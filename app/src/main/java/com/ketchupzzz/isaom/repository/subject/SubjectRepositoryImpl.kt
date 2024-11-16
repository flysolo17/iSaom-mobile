package com.ketchupzzz.isaom.repository.subject

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.SubjectWithModulesAndActivities
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.repository.activity.ActivityRepositoryImpl.Companion.ACTIVITY_COLLECTION

import com.ketchupzzz.isaom.repository.modules.MODULE_COLLECTION
import com.ketchupzzz.isaom.utils.generateRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SubjectRepositoryImpl(private val firestore: FirebaseFirestore,private val storage : FirebaseStorage): SubjectRepository {

    override suspend fun createSubject(
        subjects: Subjects,
        uri: Uri,
        result: (UiState<String>) -> Unit
    ) {

        try {
            result.invoke(UiState.Loading)
            val storageRef = storage.reference
                .child(SUBJECT_COLLECTION)
                .child(subjects.id!!)
                .child("${generateRandomString(10)}.${MimeTypeMap.getFileExtensionFromUrl(uri.toString())}")
            val downloadUri = withContext(Dispatchers.IO) {
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await()
            }
            subjects.cover = downloadUri.toString()
            firestore.collection(SUBJECT_COLLECTION)
                .document(subjects.id)
                .set(subjects)
                .await()
            result.invoke(UiState.Success("Successfully Added!"))
        } catch (e: FirebaseException) {
            result.invoke(UiState.Error(e.message ?: "Firebase error"))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message ?: "An error occurred"))
        }
    }


    override fun getAllSubject(result: (UiState<List<Subjects>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBJECT_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Subjects::class.java)))
                }
            }
    }
    override suspend fun deleteSubject(subjects: Subjects, result: (UiState<String>) -> Unit) {
        try {
            result.invoke(UiState.Loading)
            val subjectID = subjects.id ?: throw IllegalArgumentException("Subject ID is null")

            val batch = firestore.batch()
            val subjectRef = firestore.collection(SUBJECT_COLLECTION).document(subjectID)
            batch.delete(subjectRef)

            val modulesSnapshot = firestore.collection(MODULE_COLLECTION)
                .whereEqualTo("subjectID", subjectID)
                .get()
                .await()

            modulesSnapshot.forEach {
                batch.delete(it.reference)
            }

            val activitiesSnapshot = firestore.collection(ACTIVITY_COLLECTION)
                .whereEqualTo("subjectID", subjectID)
                .get()
                .await()

            activitiesSnapshot.forEach {
                batch.delete(it.reference)
            }
            
            val subjectFolderRef = storage.reference.child(SUBJECT_COLLECTION).child(subjectID)
            val storage = subjectFolderRef.listAll().await()
            storage.items.forEach {
                it.delete().await()
            }

            batch.commit().await()

            result.invoke(UiState.Success("Subject and related data deleted successfully"))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.localizedMessage ?: "An error occurred while deleting the subject"))
        }
    }


    override suspend fun getSubject(subjectID: String, result: (UiState<Subjects?>) -> Unit) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore
            .collection(SUBJECT_COLLECTION)
            .document(subjectID)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    result.invoke(UiState.Success(it.toObject(Subjects::class.java)))
                } else {
                    result.invoke(UiState.Error("Unknown Error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message ?: "Unknown Error"))
            }

    }

    override suspend fun getSubjectWithModulesAndActivities(
        subjectID: String,
        result: (UiState<SubjectWithModulesAndActivities>) -> Unit
    ) {
        try {

            val subjectSnapshot = firestore.collection(SUBJECT_COLLECTION).document(subjectID).get().await()
            val subject = subjectSnapshot.toObject(Subjects::class.java)

            if (subject == null) {
                result(UiState.Error("Subject not found"))
                return
            }

            // Fetch the modules collection
            val modulesSnapshot = firestore.collection(MODULE_COLLECTION)
                .whereEqualTo("subjectID", subjectID)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val modules = modulesSnapshot.toObjects(Modules::class.java)

            // Fetch the activities collection
            val activitiesSnapshot = firestore.collection(ACTIVITY_COLLECTION)
                .whereEqualTo("subjectID", subjectID)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val activities = activitiesSnapshot.toObjects(Activity::class.java)

            // Create the SubjectWithModulesAndActivities object
            val subjectWithModulesAndActivities = SubjectWithModulesAndActivities(
                subjects = subject,
                modules = modules,
                activities = activities
            )

            // Return the result
            result(UiState.Success(subjectWithModulesAndActivities))
        } catch (e: Exception) {
            Log.d(SUBJECT_COLLECTION,e.message.toString())
            result(UiState.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun updateSubject(
        id: String,
        name: String,
        uri: Uri?,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.Loading)

        try {
            val updates = mutableMapOf<String, Any>("name" to name)

            uri?.let {
                val fileName = "${generateRandomString(10)}.${MimeTypeMap.getFileExtensionFromUrl(it.toString())}"
                val storageRef = storage.reference.child(SUBJECT_COLLECTION).child(id).child(fileName)
                val downloadUri = withContext(Dispatchers.IO) {
                    storageRef.putFile(it).await()
                    storageRef.downloadUrl.await()
                }
                updates["cover"] = downloadUri.toString()
            }

            firestore.collection(SUBJECT_COLLECTION)
                .document(id)
                .update(updates)
                .await()

            result.invoke(UiState.Success("Successfully Updated!"))

        } catch (e: FirebaseException) {
            result.invoke(UiState.Error(e.message ?: "Firebase error"))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message ?: "An error occurred"))
        }
    }

    override suspend fun getSubjectBySectionID(
        sectionID: String,
        result: (UiState<List<Subjects>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore
            .collection(SUBJECT_COLLECTION)
            .whereEqualTo("sectionID",sectionID)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Subjects::class.java)))
                }
            }
    }

    override suspend fun getMySubjects(
        studentID: String,
        result: (UiState<List<Subjects>>) -> Unit,
    ) {
        result.invoke(UiState.Loading)
        firestore
            .collection(SUBJECT_COLLECTION)
            .whereArrayContains("students",studentID)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Subjects::class.java)))
                }
            }
    }

    override suspend fun getSubjectByCode(
        studentID: String,
        sections: List<String>,
        code: String,
        result: (UiState<Subjects>) -> Unit
    ) {
        try {
            if (sections.isEmpty()) {
                result.invoke(UiState.Error("No Sections yet!"))
                return
            }
            result.invoke(UiState.Loading)
            val subjects = firestore
                .collection(SUBJECT_COLLECTION)
                .whereIn("sectionID",sections)
                .whereEqualTo("code",code)
                .get()
                .await()
                .toObjects<Subjects>()
            if (subjects.isEmpty()) {
                result.invoke(UiState.Error("No Subject Found!"))
                return
            }
            subjects.forEach {
                if (it.students.contains(studentID)) {
                    result.invoke(UiState.Error("You already joined the subject"))
                    return
                }
            }
            val subject = subjects.first()
            result.invoke(UiState.Success(subject))
        } catch (e : Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }


    override suspend fun joinSubject(
        studentID: String,
        subjectID: String,
        result: (UiState<String>) -> Unit,
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBJECT_COLLECTION).document(subjectID)
            .update("students",FieldValue.arrayUnion(studentID))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Added"))
                } else {
                    result.invoke(UiState.Error("Unknown Error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }


    }

    companion object {
        const val SUBJECT_COLLECTION = "subjects"
    }
}