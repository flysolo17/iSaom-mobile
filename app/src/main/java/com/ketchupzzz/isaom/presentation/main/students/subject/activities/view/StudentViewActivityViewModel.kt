package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.submissions.Submissions
import com.ketchupzzz.isaom.repository.activity.ActivityRepository

import com.ketchupzzz.isaom.repository.submission.SubmissionRepository
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val submissionRepository: SubmissionRepository,
) : ViewModel() {
    var state by mutableStateOf(StudentViewActivityState())
    fun events(e : StudentViewActivityEvents) {
        when(e) {
            is StudentViewActivityEvents.OnGetActivityQuestions -> getQuestions(e.activityID)
            is StudentViewActivityEvents.OnUpdateAnswers -> updateAnswer(e.questionID,e.answer)
            is StudentViewActivityEvents.OnSubmitAnswer -> submit(e.activity)
        }
    }

    //    val id : String ? = null,
//    val studentID : String? = null,
//    val activityID : String ? = null,
//    val subjectID : String ? = null
//    val activityName : String ? = null,
//    val subjectName : String ? = null,
//    val points : Double ? = 0.00,
//    val answerSheet : Map<String,String> = hashMapOf(),
    private fun submit(activity: Activity) {
        val studentID = FirebaseAuth.getInstance().currentUser?.uid
        if (studentID.isNullOrEmpty()) {
            return
        }
        var points = 0
        var maxPoints = 0
        state.questions.forEach {
            maxPoints += it.points
            val answer = state.answers[it.id]
            if (answer == it.answer) {
                points += it.points
            }
        }
        val submissions = Submissions(
            id = generateRandomString(),
            studentID = studentID,
            activityID = activity.id,
            subjectID = activity.subjectID,
            activityName = activity.title,
            points = points,
            maxPoints = maxPoints,
            answerSheet = state.answers
        )
        viewModelScope.launch {
            submissionRepository.createSubmission(submissions = submissions) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message,
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        isSubmitted = it.data
                    )
                }
            }
        }
    }

    private fun updateAnswer(questionID: String, answer: String) {
        val updatedAnswers = state.answers.toMutableMap()
        updatedAnswers[questionID] = answer

        state = state.copy(answers = updatedAnswers)
    }



    private fun getQuestions(activityID: String) {
        viewModelScope.launch {
            activityRepository.getQuestionByActivityID(activityID) {
                state = when(it) {
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
                        questions = it.data,
                    )
                }
            }
        }
    }
}