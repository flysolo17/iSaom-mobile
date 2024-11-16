package com.ketchupzzz.isaom.repository.modules

import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.models.subject.module.ModuleWithContents
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.repository.subject.SubjectRepositoryImpl.Companion.SUBJECT_COLLECTION
import com.ketchupzzz.isaom.utils.generateRandomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


const val MODULE_COLLECTION ="modules";
const val CONTENT_COLLECTION ="contents";
class ModuleRepositoryImpl(private val firestore: FirebaseFirestore,private  val storage : FirebaseStorage): ModuleRepository {

    override suspend fun getAllModules(
        subjectID: String,
        result: (UiState<List<Modules>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(MODULE_COLLECTION)
            .whereEqualTo("subjectID", subjectID)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e(MODULE_COLLECTION, it.message, it)
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Modules::class.java)))
                }
            }
    }

    override suspend fun createModule(
        modules: Modules,
        uri: Uri,
        result: (UiState<String>) -> Unit
    ) {

        try {
            val id = modules.id ?: generateRandomString()
            result.invoke(UiState.Loading)
            val storageRef = storage.reference
                .child(SUBJECT_COLLECTION)
                .child(id)
                .child("${generateRandomString(10)}.${MimeTypeMap.getFileExtensionFromUrl(uri.toString())}")
            val downloadUri = withContext(Dispatchers.IO) {
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await()
            }

            modules.content = downloadUri.toString()
            firestore.collection(MODULE_COLLECTION)
                .document(id)
                .set(modules)
                .await()
            result.invoke(UiState.Success("Successfully Added!"))
        } catch (e: FirebaseException) {
            result.invoke(UiState.Error(e.message ?: "Firebase error"))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message ?: "An error occurred"))
        }
    }


    override suspend fun deleteModule(modules: Modules, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        modules.content?.let {
            storage.getReferenceFromUrl(it).delete().await()
        }

        firestore.collection(MODULE_COLLECTION)
            .document(modules.id!!)
            .delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Successfully Deleted"))
            }
            .addOnFailureListener { firestoreError ->
                result.invoke(UiState.Error("Failed to delete module: ${firestoreError.message}"))
            }
    }

//    override suspend fun createModule(
//        modules: Modules,
//        result: (UiState<String>) -> Unit
//    ) {
//        result.invoke(UiState.Loading)
//        firestore.collection(MODULE_COLLECTION)
//            .document(modules.id ?: generateRandomString())
//            .set(modules)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    result.invoke(UiState.Success("Successfully Created"))
//                } else {
//                    result.invoke(UiState.Error("Unknown error!"))
//                }
//            }.addOnFailureListener {
//                result.invoke(UiState.Error(it.message.toString()))
//            }
//    }

//    override suspend fun deleteModule(moduleID: String, result: (UiState<String>) -> Unit) {
//        try {
//            result.invoke(UiState.Loading)
//            val batch = firestore.batch()
//            val mainRef = firestore.collection(MODULE_COLLECTION).document(moduleID)
//
//            batch.delete(mainRef)
//
//
//
//            storage.getReference(MODULE_COLLECTION).child(moduleID).delete().await()
//
//            // Invoke success result
//            result.invoke(UiState.Success("Module deleted successfully"))
//        } catch (e: Exception) {
//            // Invoke error result
//            result.invoke(UiState.Error(e.message ?: "Unknown error"))
//        }
//    }


    override suspend fun createContent(
        moduleID: String,
        content: Content,
        uri: Uri?,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            uri?.let {
                val name = "${generateRandomString(10)}.${MimeTypeMap.getFileExtensionFromUrl(uri.toString())}"
                val storageRef = storage.reference.child(MODULE_COLLECTION).child(moduleID).child(name)
                val downloadUri = withContext(Dispatchers.IO) {
                    storageRef.putFile(uri).await()
                    storageRef.downloadUrl.await()
                }
                content.image = downloadUri.toString()
            }

            firestore.collection(MODULE_COLLECTION)
                .document(moduleID)
                .collection(CONTENT_COLLECTION)
                .document(content.id ?: "")
                .set(content)
                .await()

            result.invoke(UiState.Success("Successfully Added"))
        } catch (e: Exception) {
            Log.e(CONTENT_COLLECTION,e.localizedMessage.toString(),e)
            result.invoke(UiState.Error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun deleteContent(
        moduleID: String,
        content: Content,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(MODULE_COLLECTION)
            .document(moduleID)
            .collection(CONTENT_COLLECTION)
            .document(content.id!!)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Deleted"))
                } else {
                    result.invoke(UiState.Error("Unknown Error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message ?: "Unknown error"))
            }
    }

    override suspend fun getModule(moduleID: String, result: (UiState<Modules?>) -> Unit) {
        result.invoke(UiState.Loading)
        delay(1000)
        firestore.collection(MODULE_COLLECTION)
            .document(moduleID)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObject(Modules::class.java)))
                    Log.d(MODULE_COLLECTION,it.toObject(Modules::class.java).toString())
                }
                error?.let {
                    Log.e(MODULE_COLLECTION,it.message,it)
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }



    override fun updateLock(moduleID: String, lock: Boolean, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(MODULE_COLLECTION)
            .document(moduleID)
            .update("open",!lock)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Updated!"))
                } else {
                    result.invoke(UiState.Error("Unknown Error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }
    }

    override suspend fun getAllContents(
        moduleID: String,
        result: (UiState<List<Content>>) -> Unit
    ) {
        val moduleRef = firestore.collection(MODULE_COLLECTION).document(moduleID)
        moduleRef
            .collection(CONTENT_COLLECTION)
            .orderBy("createdAt",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                result.invoke(UiState.Loading)
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Content::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

}