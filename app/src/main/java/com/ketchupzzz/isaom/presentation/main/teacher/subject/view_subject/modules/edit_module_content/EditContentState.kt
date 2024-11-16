package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content

import android.net.Uri
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.Alignments
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.ListTypes

data class EditContentState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val uri : Uri? = null,
    val title : String = "",
    val desc : String = "",
    val alignments: Alignments = Alignments.START,
    val listTypes: ListTypes = ListTypes.NONE,
    val boldSelected  : Boolean = false,
    val italicSelected : Boolean = false,
    val underlineSelected : Boolean = false,
    val titleSelected  : Boolean = false,
    val subtitleSelected  : Boolean = false,
    val textColorSelected : Boolean = false,
    val linkSelected  : Boolean = false,
    val alignmentSelected  : Boolean = false,
)
