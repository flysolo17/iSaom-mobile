package com.ketchupzzz.isaom.presentation.main.students.subject.activities.view

import com.ketchupzzz.isaom.models.subject.activities.Activity


sealed interface StudentViewActivityEvents  {
    data class OnGetActivityQuestions(
        val activityID : String
    )   : StudentViewActivityEvents

    data class OnUpdateAnswers(val questionID : String , val answer : String) :
        StudentViewActivityEvents

    data class OnSubmitAnswer(val activity : Activity) : StudentViewActivityEvents
}