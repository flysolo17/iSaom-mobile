package com.ketchupzzz.isaom.presentation.main.students.subject.view_subject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectEvent
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectState
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.activity.ActivityRepository
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.modules.ModuleRepository
import com.ketchupzzz.isaom.repository.subject.SubjectRepository
import com.ketchupzzz.isaom.repository.submission.SubmissionRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class StudentViewSubjectViewModel @Inject constructor(
    private val authRepository: AuthRepository,
     private val moduleRepository: ModuleRepository,
    private val activityRepository: ActivityRepository,
    private val subjectRepository: SubjectRepository,
    private val submissionsRepository: SubmissionRepository
) : ViewModel() {
    var state by mutableStateOf(StudentViewSubjectState())

    fun events(e : StudentViewSubjectEvent) {
        when(e) {
            is StudentViewSubjectEvent.OnGetSubjectActivities -> getActivities(e.subjectID)
            is StudentViewSubjectEvent.OnGetSubjectModules -> getModules(e.subjectID)
            is StudentViewSubjectEvent.OnGetSubject -> getSubjectByID(e.subjectID)
            is StudentViewSubjectEvent.OnGetSubmissions -> getSubmissions()
        }
    }

    private fun getSubmissions() {
        val studentID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val subjectID = state.subject?.id ?: ""
        viewModelScope.launch {
            submissionsRepository.getAllSubmissionsByStudentID(subjectID,studentID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingSubmissions = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isGettingSubmissions = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isGettingSubmissions = false,
                        errors = null,
                        submissions = it.data
                    )
                }
            }
        }
    }

    private fun getSubjectByID(id: String) {
        viewModelScope.launch {
            subjectRepository.getSubject(subjectID = id) {
                state = when(it){
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true
                    )
                    is UiState.Success ->state.copy(
                        isLoading = false,
                        errors = null,
                        subject = it.data
                    )
                }
            }
        }
    }

    private fun getActivities(subjectID : String) {
        viewModelScope.launch {
            activityRepository.getAllActivities(subjectID) {
                state = when(it) {
                    is UiState.Error -> state.copy(isGettingActivities = false, errors = it.message)
                    is UiState.Loading -> state.copy(isGettingActivities = true, errors = null)
                    is UiState.Success -> state.copy(isGettingActivities = false, errors = null, activities = it.data)
                }
            }
        }
    }

    private fun getModules(subjectID: String) {
        viewModelScope.launch {
            moduleRepository.getAllModules(subjectID) {
                state = when(it) {
                    is UiState.Error -> state.copy(isGettingModules = false, errors = it.message)
                    is UiState.Loading -> state.copy(isGettingModules = true, errors = null)
                    is UiState.Success -> state.copy(isGettingModules = false, errors = null, modules = it.data)
                }
            }
        }
    }
}