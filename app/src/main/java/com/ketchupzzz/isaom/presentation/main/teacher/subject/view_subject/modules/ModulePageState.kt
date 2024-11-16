package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules

import android.net.Uri

data class ModulePageState(
    val isLoading : Boolean = false,
    val title : String = "",
    val desc : String = "",
    val uri  : Uri?  = null,
    val pdfName : String = "",
)
