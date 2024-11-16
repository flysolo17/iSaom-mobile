package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectState
import com.ketchupzzz.isaom.repository.activity.ActivityRepository
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.modules.ModuleRepository
import com.ketchupzzz.isaom.repository.subject.SubjectRepository
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ViewSubjectViewModel @Inject constructor(
     private val subjectRepository: SubjectRepository,
    private  val moduleRepository: ModuleRepository,
    private val activityRepository: ActivityRepository,
    private  val authRepository: AuthRepository,
) : ViewModel() {
    var state by mutableStateOf(ViewSubjectState())
    init {

    }
    fun events(events: ViewSubjectEvents) {
        when(events) {
            is ViewSubjectEvents.OnGetSubjectByID -> getSubjectWithID(events.id)

            is ViewSubjectEvents.OnCreateModule -> createModule(events.module,events.uri,events.result)


            is ViewSubjectEvents.OnGetActivitiesBySubjectID -> getActivities(events.subjectID)
            is ViewSubjectEvents.OnGetModulesBySubjectID -> getModules(events.subjectID)
            is ViewSubjectEvents.OnDeleteSubject -> deleteSubject(events.subjects)
            is ViewSubjectEvents.OnDeleteModule -> deleteModule(events.module,events.context)
            is ViewSubjectEvents.UpdateLock -> updateLock(events.moduleID,events.lock,events.context)
            is ViewSubjectEvents.OnCreateActivity -> createActivity(events.activity,events.result)
            is ViewSubjectEvents.deleteActivity -> deleteActivity(events.activityID,events.context)
            is ViewSubjectEvents.updateActivityLock -> updateActivityLock(events.activity,events.context)
        }
    }

    private fun updateActivityLock(activity: Activity,context: Context) {
        viewModelScope.launch {
            activityRepository.updateLock(activity) {
                when(it) {
                    is UiState.Error -> {
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun deleteActivity(activityID: String,context: Context) {
        viewModelScope.launch {
            activityRepository.deleteActivity(activityID = activityID) {
                state = when(it) {
                    is UiState.Error -> state.copy(isLoading = false, errors = it.message)
                    UiState.Loading -> state.copy(isLoading = true, errors = null)
                    is UiState.Success -> {
                        Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                        state.copy(isLoading = false, errors = null)
                    }
                }
            }
        }
    }

    private fun createActivity(activity: Activity ,result  : (UiState<String>) -> Unit) {
        val newActivity = activity.copy(
            subjectID = state.subjectID
        )
        return activityRepository.createActivity(newActivity,result)
    }

    private fun updateLock(moduleID: String, lock: Boolean,context: Context) {
            moduleRepository.updateLock(moduleID,lock) {
           when(it) {
                    is UiState.Error -> {
                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun deleteModule(module : Modules,context : Context) {
        viewModelScope.launch {
            moduleRepository.deleteModule(module) {
                state = when(it) {
                    is UiState.Error -> state.copy(isLoading = false, errors = it.message)
                    UiState.Loading -> state.copy(isLoading = true, errors = null)
                    is UiState.Success -> {
                        Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                        state.copy(isLoading = false, errors = null)
                    }
                }
            }
        }
    }

    private fun deleteSubject(subjects: Subjects) {
        viewModelScope.launch {
            subjectRepository.deleteSubject(subjects) {
               state =  when(it) {
                    is UiState.Error -> state.copy(
                        isDeleting = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state.copy(
                        isDeleting = true,
                        errors =null,
                    )
                    is UiState.Success -> state.copy(
                        isDeleting = false,
                        errors = null,
                        deletionSuccess = it.data,
                    )
                }
            }
        }
    }

    private fun getActivities(subjectID: String) {

        viewModelScope.launch {
            activityRepository.getAllActivities(subjectID) {
                state =  when(it) {
                    is UiState.Error -> state.copy(isActivityLoading = false, errors = it.message)
                    is UiState.Loading -> state.copy(isActivityLoading = true, errors = null)
                    is UiState.Success -> state.copy(
                        isActivityLoading = false,
                        errors = null,
                        activities = it.data
                    )
                }
            }
        }
    }

    private fun getModules(subjectID: String) {
        viewModelScope.launch {
            moduleRepository.getAllModules(subjectID) {
                state =  when(it) {
                    is UiState.Error -> state.copy(isModuleLoading = false, errors = it.message)
                   is UiState.Loading -> state.copy(isModuleLoading = true, errors = null)
                    is UiState.Success -> state.copy(
                        isModuleLoading = false,
                        errors = null,
                        modules = it.data
                    )
                }
            }
        }
    }

    private fun createModule(modules: Modules,uri: Uri,result: (UiState<String>) -> Unit) {
        val newModule = modules.copy(
            id = generateRandomString(),
            subjectID = state.subjectID
        )
        viewModelScope.launch {
            moduleRepository.createModule(modules = newModule,uri,result)
        }

    }


    private fun getSubjectWithID(id: String) {
        state = state.copy(subjectID = id)
        viewModelScope.launch {
            subjectRepository.getSubject(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(errors = it.message, isLoading = false)
                    is UiState.Loading -> state.copy(errors = null, isLoading = true)
                    is UiState.Success -> {
                        if (it.data != null) {
                            getStudents(it.data)
                        }
                        state.copy(
                            errors = null,
                            isLoading = false,
                            subjects = it.data,
                        )
                    }
                }
            }
        }
    }

    private fun getStudents(subjects: Subjects) {
        viewModelScope.launch {
            authRepository.getStudentsInSubject(subjects.students) {
                if (it is UiState.Success) {
                    state = state.copy(
                        students = it.data
                    )
                }
            }
        }
    }
}