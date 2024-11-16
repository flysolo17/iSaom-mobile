package com.ketchupzzz.isaom.presentation.main.students.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.models.SourceAndTargets
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.subject.SubjectRepository
import com.ketchupzzz.isaom.repository.translator.TranslatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class StudentHomeViewModel @Inject constructor(
     private val authRepository: AuthRepository,
     private val translatorRepository: TranslatorRepository,
     private val subjectRepository: SubjectRepository
) : ViewModel() {
    var state by mutableStateOf(StudentHomeState())

     fun events(events: StudentHomeEvents) {
          when(events) {
              is StudentHomeEvents.OnJoinSubject -> joinSubject(events.studentID,events.subjectID)
              is StudentHomeEvents.OnGetSubjects -> getSubjects(events.studentID)
              is StudentHomeEvents.OnSetUsers -> state  = state.copy(users = events.users)
              is StudentHomeEvents.OnFindSubjects -> finSubject(events.code)
              is StudentHomeEvents.UpdateSubjectFound -> state = state.copy(
                  subjectFound = events.subjects
              )
          }
     }

    private fun finSubject(code: String) {
        viewModelScope.launch {
            if (state.users != null) {
                subjectRepository.getSubjectByCode(
                    studentID = state.users?.id ?: "",
                    sections = state.users?.sections ?: emptyList(),
                    code = code
                ) {
                    when(it) {
                        is UiState.Error -> state = state.copy(
                            isLoading = false,
                            error = it.message
                        )
                        UiState.Loading -> state = state.copy(
                            isLoading = true,
                            error = null,

                        )
                        is UiState.Success -> state = state.copy(
                            isLoading = false,
                            error = null,
                            subjectFound = it.data
                        )
                    }
                }
            }

        }
    }

    private fun joinSubject(studentID: String,subjectID : String) {
        viewModelScope.launch {
            subjectRepository.joinSubject(
                studentID = studentID,
                subjectID = subjectID
            ) {
                state = when(it) {
                    is UiState.Error -> state.copy(error = it.message, isLoading = false)
                    UiState.Loading -> state.copy(isLoading = true, error = null)
                    is UiState.Success -> state.copy(isLoading = false, error = null, subjectFound =null)
                }
            }
        }
    }


    private fun getSubjects(studentID : String) {
        viewModelScope.launch {
            subjectRepository.getMySubjects(studentID) {
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
                        subjects = it.data
                    )
                }
            }
        }
     }

}