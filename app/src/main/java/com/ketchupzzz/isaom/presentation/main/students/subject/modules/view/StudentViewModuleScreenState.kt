package com.ketchupzzz.isaom.presentation.main.students.subject.modules.view

import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.models.subject.module.Modules

data class StudentViewModuleScreenState(
    val isLoading : Boolean = false,
    val module : Modules? = null,
    val errors : String ? = null
)