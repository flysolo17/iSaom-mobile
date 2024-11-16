package com.ketchupzzz.isaom.presentation.main.students.subject.modules.view

sealed interface StudentViewModuleScreenEvents {
    data class OnGetContents(
        val moduleID : String
    ) : StudentViewModuleScreenEvents
}