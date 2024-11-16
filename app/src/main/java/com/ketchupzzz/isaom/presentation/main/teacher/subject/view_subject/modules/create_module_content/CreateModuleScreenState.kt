package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content

import android.net.Uri


data class CreateModuleScreenState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val uri : Uri ? = null,
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



enum class ListTypes {
    NONE,
    UNORDERED,
    ORDERED
}
enum class ActionButtons {
    BOLD,
    ITALIC,
    UNDERLINE,
    TITLE,
    SUBTITLE,
    TEXT_COLOR,
    LINK,
    ALIGNMENT
}

enum class Alignments {
    START,
    CENTER,
    END,
    JUSTIFY
}