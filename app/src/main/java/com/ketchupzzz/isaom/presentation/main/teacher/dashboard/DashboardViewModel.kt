package com.ketchupzzz.isaom.presentation.main.teacher.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.auth.AuthRepository
import com.ketchupzzz.isaom.repository.sections.SectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class DashboardViewModel @Inject constructor(
    private  val authRepository: AuthRepository,
     private val sectionRepository: SectionRepository
) : ViewModel() {
    var state by mutableStateOf(DashboardState())
    fun events(event: DashboardEvents) {
        when(event) {
            is DashboardEvents.OnGetAllSectionWithSubjects -> getAllSectionWithSubject(event.sections)
            is DashboardEvents.OnSetUsers -> {
                state = state.copy(users = event.users)
            }
        }
    }

    private fun getAllSectionWithSubject(sections : List<String>) {
        viewModelScope.launch {
            sectionRepository.getAllSectionsByTeacher(sections) {
                state = when(it) {
                    is UiState.Error -> state.copy(isLoading = false, errors = it.message)
                    is UiState.Loading -> state.copy(isLoading = true, errors = null)
                    is UiState.Success ->  {
                        state.copy(
                            isLoading = false,
                            errors = null,
                            sections = it.data,
                        )
                    }
                }
            }
        }
    }


}