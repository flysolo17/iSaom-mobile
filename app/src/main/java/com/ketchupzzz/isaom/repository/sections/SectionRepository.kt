package com.ketchupzzz.isaom.repository.sections

import com.ketchupzzz.isaom.models.sections.SectionWithStudents
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.sections.SectionWithSubjects
import com.ketchupzzz.isaom.models.sections.SectionWithSubjectsAndStudents
import com.ketchupzzz.isaom.models.sections.Sections

interface SectionRepository {
    fun getSectionWithSubject() : List<SectionWithSubjects>
    fun setSectionWithSubject(sectionWithSubject : List<SectionWithSubjects>)
    fun getAllSections(result :(UiState<List<Sections>>) -> Unit)
    suspend fun getAllSectionsByTeacher(
        sections  : List<String>,
        result: (UiState<List<Sections>>) -> Unit
    )

    suspend  fun getAllSectionWithSubjects(userID : String,result: (UiState<List<SectionWithSubjects>>) -> Unit)

    suspend fun getAllSectionWithSubjectsAndStudents(
        userID: String,
        result: (UiState<List<SectionWithSubjectsAndStudents>>) -> Unit
    )

    suspend fun getSectionsWithStudent(
        userID: String,
        result: (UiState<List<SectionWithStudents>>) -> Unit
    )

    suspend fun getSectionWithSubjects(
        sectionID : String,
        result: (UiState<SectionWithSubjects>) -> Unit
    )
}