package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module

import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.models.subject.module.ModuleWithContents
import com.ketchupzzz.isaom.models.subject.module.Modules

data class ViewModuleState(
    val isLoading : Boolean = false,
    val module: Modules ? = null,
    val contents  : List<Content> = emptyList(),
    val error : String ? = null,
    val moduleID : String  = ""
)