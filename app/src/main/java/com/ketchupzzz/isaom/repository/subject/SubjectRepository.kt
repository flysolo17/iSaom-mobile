package com.ketchupzzz.isaom.repository.subject

import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.SubjectWithModulesAndActivities
import com.ketchupzzz.isaom.models.subject.Subjects


interface SubjectRepository  {
    suspend fun createSubject(subjects: Subjects, uri : Uri, result : (UiState<String>) -> Unit)
    fun getAllSubject(result: (UiState<List<Subjects>>) -> Unit)
    suspend  fun deleteSubject(subjects: Subjects, result: (UiState<String>) -> Unit)
    suspend fun getSubject(subjectID : String,result: (UiState<Subjects?>) -> Unit)
    suspend fun getSubjectWithModulesAndActivities(subjectIS : String,result: (UiState<SubjectWithModulesAndActivities>) -> Unit)

    suspend fun updateSubject(id : String,name : String,uri: Uri? ,result: (UiState<String>) -> Unit)


    suspend fun getSubjectBySectionID(
        sectionID : String,
        result: (UiState<List<Subjects>>) -> Unit
    )

    suspend fun getMySubjects(studentID: String,result: (UiState<List<Subjects>>) -> Unit)
    suspend fun getSubjectByCode(studentID: String,sections : List<String>,code: String,result: (UiState<Subjects>) -> Unit)
    suspend fun joinSubject(studentID : String,subjectID  : String,result: (UiState<String>) -> Unit)



}
