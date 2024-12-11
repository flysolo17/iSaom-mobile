package com.ketchupzzz.isaom.presentation.main.profle

import android.net.Uri


sealed interface ProfileEvents {
    data object OnLoggedOut : ProfileEvents
    data object GeUserInfg : ProfileEvents
    data class SelectProfile(val uri : Uri) : ProfileEvents
}