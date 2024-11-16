package com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject

import android.net.Uri


sealed interface EditSubjectEvents {
    data class OnGetSubjectByID(val id : String) : EditSubjectEvents
    data class OnUpdateSubject(val id : String ,val name : String,val uri : Uri ? ) :
        EditSubjectEvents

    data class OnCoverSelected(val uri: Uri) : EditSubjectEvents
    data class OnNameChanged(val name : String) : EditSubjectEvents
}