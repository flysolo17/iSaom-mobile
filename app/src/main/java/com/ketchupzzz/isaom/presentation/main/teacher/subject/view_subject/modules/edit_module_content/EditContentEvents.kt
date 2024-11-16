package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.ActionButtons
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.ListTypes

sealed interface EditContentEvents {
    data class OnInitializeContent(val content: Content) : EditContentEvents
    data class OnActionButtonClicked(val actionButtons: ActionButtons) : EditContentEvents
    data class OnListTypesSelected(val types: ListTypes) : EditContentEvents
    data class OnTitleChange(val title : String) : EditContentEvents
    data class OnDescChange(val desc : String) : EditContentEvents
    data class OnImagePicked(val uri : Uri? ) : EditContentEvents
    data class OnCreateContent(
        val moduleID : String,
        val content : Content,
        val body : String,
        val uri : Uri?,
        val context: Context,
        val navHostController: NavHostController
    ) : EditContentEvents
}