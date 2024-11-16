package com.ketchupzzz.isaom.presentation.main.teacher.dashboard

import com.ketchupzzz.isaom.models.Users

sealed interface DashboardEvents {
    data class OnGetAllSectionWithSubjects(
        val sections : List<String>,
    ) : DashboardEvents
    data class OnSetUsers(
        val users: Users ?
    ) : DashboardEvents
}