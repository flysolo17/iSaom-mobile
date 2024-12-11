package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.create_question

import android.net.Uri


data class CreateQuestionState(
    val isLoading: Boolean = false,
    val title: String = "",
    val desc: String = "",
    val uri: Uri? = null,
    val actions: List<String> = emptyList(),
    val choices: List<String> = emptyList(),
    val answer: String = "",
    val points: Int = 0,
    val expanded: Boolean = false,
    val labels  : List<String> = listOf(
         "asul", "berde", "duwwa", "duyaw", "innem", "kabalyu", "kalding",
        "kuadrado", "kulay kahel", "kulay mara-daga", "kulay ube", "kulibangbang", "kuton",
        "lima", "mara-itlog", "marabituen", "maradiamante", "maysa", "nagbukel", "nalabbaga",
        "nangisit", "nuang", "pito", "puraw", "rektangulo", "sangapulo", "siam", "tallo",
        "triangulo", "tukak", "tuwwatu", "uleg", "uppat", "walo"
    )
)
