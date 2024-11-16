package com.ketchupzzz.isaom.presentation.auth.edit_profile

import android.net.Uri
import com.ketchupzzz.isaom.models.Gender


sealed interface EditProfileEvents  {

    data class OnNameChange(val name : String) : EditProfileEvents

    data class OnSaveChanges(
        val uid : String,
        val name : String,


    ) : EditProfileEvents
}