package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module

import android.content.Context
import com.ketchupzzz.isaom.models.subject.module.Content

sealed interface ViewModuleEvents {
    data class ViewModuleWithContents(val moduleID : String) : ViewModuleEvents
    data class DeleteContent(val moduleID: String,val content: Content,val context : Context) :
        ViewModuleEvents
}