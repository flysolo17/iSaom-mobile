package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.edit_question

import android.net.Uri


data class EditQuestionState(
    val isLoading : Boolean = false,
    val title : String = "",
    val desc : String = "",
    val uri : Uri? = null,
    val actions : List<String> = emptyList(),
    val choices : List<String> = emptyList(),
    val answer : String = "",
    val points : Int = 0,
    val expanded : Boolean = false
)
