package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content.data

import android.os.Parcelable
import com.ketchupzzz.isaom.models.subject.module.Content
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentWithModuleID(
    val moduleID : String,
    val content: Content
) : Parcelable
