package com.ketchupzzz.isaom.presentation.auth.edit_profile

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.utils.Fullname

data class EditProfileState(
    val isLoading : Boolean = false,
    val name : Fullname = Fullname(),
    val gender : Gender ?= null,

    val errors : String ? = null,
    val isSaving : Boolean = false,
    val isDoneSaving : String ? = null
)