package com.ketchupzzz.isaom.repository.sections

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.sections.SectionWithStudents
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.sections.SectionWithSubjects
import com.ketchupzzz.isaom.models.sections.SectionWithSubjectsAndStudents
import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.repository.auth.USERS_COLLECTION
import com.ketchupzzz.isaom.repository.subject.SubjectRepositoryImpl.Companion.SUBJECT_COLLECTION
import kotlinx.coroutines.tasks.await


const val SECTION_COLLECTION = "sections"
class SectionRepositoryImpl(val firestore : FirebaseFirestore): SectionRepository {
    private var _sectionWithSubject: MutableState<List<SectionWithSubjects>> = mutableStateOf(
        emptyList()
    )

    override fun getSectionWithSubject(): List<SectionWithSubjects> {
        return _sectionWithSubject.value
    }

    override fun setSectionWithSubject(sectionWithSubject: List<SectionWithSubjects>) {
        _sectionWithSubject.value  =sectionWithSubject
    }

    override fun getAllSections(result: (UiState<List<Sections>>) -> Unit) {
        firestore.collection(SECTION_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    val sectionList = it.toObjects(Sections::class.java)
                    result.invoke(UiState.Success(sectionList))
                }
            }
    }

    override suspend fun getAllSectionsByTeacher(
        sections: List<String>,
        result: (UiState<List<Sections>>) -> Unit,
    ) {
        try {
            result.invoke(UiState.Loading)
            if (sections.isEmpty()) {
                result.invoke(UiState.Success(emptyList()))
                return
            }
            val sections: List<Sections> = firestore
                .collection(SECTION_COLLECTION)
                .whereIn("id", sections)
                .get()
                .await()
                .toObjects<Sections>()
            result(UiState.Success(sections))
        } catch (e : Exception) {
            Log.e(SECTION_COLLECTION,e.message.toString())
            result(UiState.Error(e.message.toString()))
        }


    }

    override suspend fun getAllSectionWithSubjects(userID: String, result: (UiState<List<SectionWithSubjects>>) -> Unit) {
        result.invoke(UiState.Loading)
        try {
            val sections: List<Sections> = firestore.collection(SECTION_COLLECTION)
                .whereEqualTo("teacher", userID)
                .get()
                .await()
                .toObjects<Sections>()

            val sectionIds = sections.map { it.id ?: "" }
            firestore.collection(SUBJECT_COLLECTION)
                .whereIn("sectionID", sectionIds)
                .addSnapshotListener { value, error ->
                   error?.let {
                       result.invoke(UiState.Error(error.message ?: "Unknown error"))
                       return@addSnapshotListener
                   }
                    value?.let {
                        val subjects: List<Subjects> = value.toObjects<Subjects>()
                        val groupedSubjects = subjects.groupBy { it.sectionID }
                        val sectionWithSubjects = sections.map { section ->
                            SectionWithSubjects(
                                sections = section,
                                subjects = groupedSubjects[section.id] ?: emptyList()
                            )
                        }
                        result.invoke(UiState.Success(sectionWithSubjects))
                    }
                }
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getAllSectionWithSubjectsAndStudents(
        userID: String,
        result: (UiState<List<SectionWithSubjectsAndStudents>>) -> Unit,
    ) {
    }

    override suspend fun getSectionsWithStudent(
        userID: String,
        result: (UiState<List<SectionWithStudents>>) -> Unit,
    ) {
        try {
            result(UiState.Loading)
            val sections: List<Sections> = firestore.collection(SECTION_COLLECTION)
                .whereEqualTo("teacher", userID)
                .get()
                .await()
                .toObjects<Sections>()
            val sectionWithStudents = mutableListOf<SectionWithStudents>()
            sections.forEach {
                val students = firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("type",UserType.STUDENT)
                    .whereEqualTo("sectionID",it.id)
                    .get()
                    .await()
                    .toObjects<Users>()
                sectionWithStudents.add(
                    SectionWithStudents(
                        sections = it,
                        students = students
                    )
                )
            }
            result(UiState.Success(sectionWithStudents))
        } catch (e : Exception) {
            Log.e(SECTION_COLLECTION,e.message.toString())
            result(UiState.Error(e.message.toString()))
        }

    }

    override suspend fun getSectionWithSubjects(
        sectionID: String,
        result: (UiState<SectionWithSubjects>) -> Unit,
    ) {
        try {
            val section = firestore.collection(SECTION_COLLECTION)
                .document(sectionID)
                .get()
                .await()
                .toObject<Sections>()
            val subjects = firestore.collection(SUBJECT_COLLECTION)
                .whereEqualTo("sectionID",sectionID)
                .get()
                .await()
                .toObjects<Subjects>()
            result.invoke(
                UiState.Success(
                    SectionWithSubjects(
                        sections = section,
                        subjects = subjects
                    )
                )
            )
        } catch (e : Exception) {
            Log.e(SECTION_COLLECTION,e.message.toString())
            result(UiState.Error(e.message.toString()))
        }
    }

}