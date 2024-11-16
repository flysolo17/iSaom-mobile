package com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject.EditSubjectEvents
import com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject.EditSubjectState
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.subject.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class EditSubjectViewModel @Inject constructor(
     private val subjectRepository: SubjectRepository
) : ViewModel() {
    var state by mutableStateOf(EditSubjectState())

    fun events(e : EditSubjectEvents) {
        when(e) {
            is EditSubjectEvents.OnGetSubjectByID -> getSubjectWithID(e.id)
            is EditSubjectEvents.OnUpdateSubject -> updateSubject(e.id,e.name,e.uri)
            is EditSubjectEvents.OnCoverSelected -> state = state.copy(cover = e.uri)
            is EditSubjectEvents.OnNameChanged ->  state = state.copy(name = e.name)
        }
    }

    private fun updateSubject(id: String, name: String, uri: Uri?) {
        viewModelScope.launch {
            subjectRepository.updateSubject(id,name,uri) {
               state= when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        error = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        error = null,
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        error = null,
                        success = it.data
                    )
                }
            }
        }
    }

    private fun getSubjectWithID(id: String) {
        viewModelScope.launch {
            subjectRepository.getSubject(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(error = it.message, isLoading = false)
                    is UiState.Loading -> state.copy(error = null, isLoading = true)
                    is UiState.Success -> state.copy(
                        error = null,
                        isLoading = false,
                        subjects = it.data,
                        name = it.data?.name ?: ""
                    )
                }
            }
        }
    }
}