package com.ketchupzzz.isaom.presentation.main.lessons

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.repository.lessons.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel

class LessonViewModel @Inject constructor(
     private val lessonRepository: LessonRepository
) : ViewModel() {
    var state by mutableStateOf(LessonState())
    init {
        events(LessonEvents.OnGetAllLessons)
    }
    fun events(event: LessonEvents){
        when(event) {
            LessonEvents.OnGetAllLessons -> fetchAllLesson()
        }
    }

    private fun fetchAllLesson() {
        lessonRepository.getAllLessons {
            when(it) {
                is UiState.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                }
                is UiState.Loading -> {
                    state = state.copy(
                        isLoading = true,
                        errors = null
                    )
                }
                is UiState.Success -> {
                    state = state.copy(
                        isLoading = false,
                        errors =null,
                        lessons =  it.data
                    )
                }
            }
        }
    }
}