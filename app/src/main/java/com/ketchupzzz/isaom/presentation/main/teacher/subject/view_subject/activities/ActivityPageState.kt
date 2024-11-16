package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities

import android.net.Uri

data class ActivityPageState(
   val isLoading :Boolean = false,
   val title : String = "",
   val desc : String = "",
   val uri : Uri ? = null,
)