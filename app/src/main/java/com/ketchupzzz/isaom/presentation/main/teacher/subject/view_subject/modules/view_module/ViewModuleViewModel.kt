package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.repository.modules.ModuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ViewModuleViewModel @Inject constructor(
     private val moduleRepository: ModuleRepository,

) : ViewModel() {
    var state by mutableStateOf(ViewModuleState())

    fun events(e: ViewModuleEvents) {
        when(e) {
            is ViewModuleEvents.ViewModuleWithContents -> getModuleWithContents(e.moduleID)
            is ViewModuleEvents.DeleteContent -> {}
        }
    }



    private fun getModuleWithContents(moduleID: String) {
        state = state.copy(
            moduleID = moduleID
        )
        viewModelScope.launch {
            moduleRepository.getModule(moduleID) {
               state =  when(it) {
                    is UiState.Error -> state.copy(isLoading = false , error = it.message)
                    is UiState.Loading -> state.copy(isLoading = true, error = null)
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        error = null,
                        module = it.data,
                    )
                }
            }
        }
    }
}