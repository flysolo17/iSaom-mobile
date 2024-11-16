package com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section.ViewSectionEvents
import com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section.ViewSectionState
import com.ketchupzzz.isaom.repository.sections.SectionRepository
import com.ketchupzzz.isaom.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ViewSectionViewModel @Inject constructor(
     private val sectionRepository: SectionRepository
) : ViewModel() {
    var state by mutableStateOf(ViewSectionState())

    fun events(e : ViewSectionEvents) {
        when(e) {
            is ViewSectionEvents.getSectionWithSubjects -> getSectionWithSubjects(e.sectionID)
        }
    }

    private fun getSectionWithSubjects(sectionID : String) {
        viewModelScope.launch {
            sectionRepository.getSectionWithSubjects(sectionID) {
                state = when (it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        sections = it.data.sections,
                        subjects = it.data.subjects
                    )
                }
            }
        }
    }
}