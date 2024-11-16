package com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject.AddSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject.AddSubjectState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.sections.SectionRepository
import com.ketchupzzz.isaom.repository.subject.SubjectRepository
import com.ketchupzzz.isaom.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel

class SubjectViewModel @Inject constructor(
private  val sectionRepository: SectionRepository,
     private val subjectRepository: SubjectRepository,
    private val authService : AuthRepository
) : ViewModel() {

    var state by mutableStateOf(AddSubjectState())
    init {
        viewModelScope.launch {
            authService.getCurrentUser {
                if (it is UiState.Success) {
                    state = state.copy(
                        users = it.data
                    )
                    events(AddSubjectEvents.OnGetAllSections(state.users?.sections ?: emptyList()))
                }

            }
        }


    }
    fun events(events: AddSubjectEvents) {
        when(events) {
            is AddSubjectEvents.OnAddingSubject -> saveSubject(events.sectionID)
            is AddSubjectEvents.OnCoverSelected -> state = state.copy(cover=events.uri)
            is AddSubjectEvents.OnNameChanged ->state = state.copy(name=events.name)
            is AddSubjectEvents.OnSectionSelected -> state = state.copy(selectedSection = events.sections)
            is AddSubjectEvents.OnGetAllSections -> getAlSections(events.sections)
        }
    }
    private fun getAlSections(sections : List<String>) {
        viewModelScope.launch {
            sectionRepository.getAllSectionsByTeacher(sections) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        error = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        error = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        error = null,
                        sections = it.data
                    )
                }
            }
        }
    }

    private fun saveSubject(sectionID : String) {
        val subject = Subjects(
            id = generateRandomString(),
            name = state.name,
            sectionID = sectionID,
            code = state.code,
            createdAt = Date()
        )
        if (state.cover === null) {
            state  = state.copy(error = "No cover selected")
            return
        }
        viewModelScope.launch {
            subjectRepository.createSubject(subject,state.cover!!) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        error =  it.message
                    )

                    is UiState.Loading -> state.copy(
                        isLoading = true,
                        error = null
                    )

                    is UiState.Success -> state.copy(
                        success = "Successfully Added",
                        isLoading = false,
                        error = null
                    )
                }
            }
        }

    }
}