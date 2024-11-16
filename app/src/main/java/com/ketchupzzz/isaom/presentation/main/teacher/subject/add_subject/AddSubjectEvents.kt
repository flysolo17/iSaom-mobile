package com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject

import android.net.Uri
import com.ketchupzzz.isaom.models.sections.Sections


sealed interface AddSubjectEvents {
    data class OnNameChanged(val name : String) : AddSubjectEvents
    data class OnCoverSelected(val uri : Uri ? ) : AddSubjectEvents
    data class OnAddingSubject(val sectionID : String): AddSubjectEvents

    data class OnSectionSelected(val sections: Sections?) : AddSubjectEvents

    data class OnGetAllSections(val sections : List<String>) : AddSubjectEvents
}