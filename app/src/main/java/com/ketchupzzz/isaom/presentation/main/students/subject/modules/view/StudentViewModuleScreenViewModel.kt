package com.ketchupzzz.isaom.presentation.main.students.subject.modules.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.presentation.main.students.subject.modules.view.StudentViewModuleScreenEvents
import com.ketchupzzz.isaom.presentation.main.students.subject.modules.view.StudentViewModuleScreenState
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.modules.ModuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class StudentViewModuleViewModel @Inject constructor(
     private val moduleRepository: ModuleRepository
) : ViewModel() {
    var state by mutableStateOf(StudentViewModuleScreenState())
    fun events(e : StudentViewModuleScreenEvents) {
        when(e) {
            is StudentViewModuleScreenEvents.OnGetContents -> getContents(e.moduleID)
        }
    }

    private fun getContents(moduleID: String) {
        viewModelScope.launch {
            moduleRepository.getModule(moduleID) {
                state= when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is UiState.Loading ->state.copy(
                        isLoading = true,
                        errors = null
                    )

                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        module = it.data
                    )
                }
            }
        }

    }
}