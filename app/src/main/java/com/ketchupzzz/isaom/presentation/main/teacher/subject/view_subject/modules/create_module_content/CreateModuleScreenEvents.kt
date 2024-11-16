package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController

sealed interface CreateModuleScreenEvents  {
    data class OnActionButtonClicked(val actionButtons: ActionButtons) : CreateModuleScreenEvents
    data class OnListTypesSelected(val types: ListTypes) : CreateModuleScreenEvents
    data class OnTitleChange(val title : String) : CreateModuleScreenEvents
    data class OnDescChange(val desc : String) : CreateModuleScreenEvents
    data class OnImagePicked(val uri : Uri? ) : CreateModuleScreenEvents
    data class OnCreateContent(
        val moduleID : String,
        val body : String,
        val uri : Uri ?,
        val context: Context,
        val navHostController: NavHostController
    ) : CreateModuleScreenEvents
}

