package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.activities.Question
import com.ketchupzzz.isaom.models.submissions.AnswersByQuestion
import com.ketchupzzz.isaom.repository.activity.ActivityRepository
import com.ketchupzzz.isaom.repository.submission.SubmissionRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ViewActivityViewModel @Inject constructor(
     private val activityRepository: ActivityRepository,
    private val submissionRepository: SubmissionRepository
) : ViewModel() {
    var state by mutableStateOf(ViewActivityState())
    
    fun events(e : ViewActivityEvents) {
        when(e) {
            is ViewActivityEvents.OnGetActivityByID -> getActivityByID(e.id)
            is ViewActivityEvents.OnSaveQuestion -> createQuestion(e.actvityID,e.question,e.uri,e.result)
            is ViewActivityEvents.OnGetQuestionByActivityID -> getQuestions(e.activityID)
            is ViewActivityEvents.OnEdeleteQuestion -> deleteQuestion(e.activityID,e.question)
            is ViewActivityEvents.OnGetAllSubmissionsByActivityID -> getSubmissions(e.id,e.question)
        }
    }

    private fun getQuestions(activityID: String) {
        viewModelScope.launch {
            activityRepository.getQuestionByActivityID(activityID) {
                when(it) {
                    is UiState.Error ->state = state.copy(isLoading = false, errors = it.message)
                    is UiState.Loading ->state = state.copy(isLoading = true, errors = null)
                    is UiState.Success -> {
                        state = state.copy(isLoading = false, errors = null, questions = it.data)
                        events(ViewActivityEvents.OnGetAllSubmissionsByActivityID(activityID,it.data))
                    }
                }
            }
        }
    }
    private fun getSubmissions(id: String, questions: List<Question>) {
        viewModelScope.launch {
            submissionRepository.getSubmissionsByActivityID(activityID = id) { result ->
                when (result) {
                    is UiState.Error -> state = state.copy(
                        isLoading = false,
                        errors = result.message
                    )

                    is UiState.Loading -> state = state.copy(
                        isLoading = true,
                        errors = null
                    )

                    is UiState.Success -> {
                        val submissionWithStudent = result.data
                        val answersByQuestion = questions.map { question ->
                            val answers = mutableMapOf<String, String>()
                            var correctAnswers = 0
                            var wrongAnswers = 0

                            submissionWithStudent.forEach { submission ->
                                val answer = submission.submissions!!.answerSheet[question.id]
                                if (answer != null) {
                                    answers[submission.submissions.id!!] = answer
                                    if (answer == question.answer) {
                                        correctAnswers++
                                    } else {
                                        wrongAnswers++
                                    }
                                }
                            }

                            AnswersByQuestion(
                                question = question,
                                answers = answers,
                                correctAnswers = correctAnswers,
                                wrongAnswers = wrongAnswers,
                                totalSubmissions = answers.size
                            )
                        }

                        state = state.copy(
                            isLoading = false,
                            errors = null,
                            submissionWithStudent = submissionWithStudent,
                            answersByQuestion = answersByQuestion
                        )
                    }
                }
            }
        }
    }

    private fun deleteQuestion(activityID: String, question: Question) {
        viewModelScope.launch {
            activityRepository.deleteQuestion(activityID = activityID, question = question) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        messages = it.data
                    )
                }
            }
        }
    }



    private fun createQuestion(activityID : String ,question: Question,uri : Uri?, result: (UiState<String>) -> Unit) {
        viewModelScope.launch {
            activityRepository.createQuestion(activityID,question,uri,result)
        }
    }

    private fun getActivityByID(id: String) {
        viewModelScope.launch {
            activityRepository.getActivityByID(id) {
                 when(it) {
                    is UiState.Error -> {
                        state = state.copy(isLoading = false, errors = it.message)
                    }

                    is UiState.Loading -> {
                        state = state.copy(isLoading = true, errors = null)
                    }

                    is UiState.Success -> {
                        state =  state.copy(
                            isLoading = false,
                            errors = null,
                            activity = it.data
                        )
                        events(ViewActivityEvents.OnGetQuestionByActivityID(id))
                    }
                }
            }
        }
    }
}