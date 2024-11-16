package com.ketchupzzz.isaom.presentation.auth.register

import com.ketchupzzz.isaom.models.FemaleSelection
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.GenderSelection
import com.ketchupzzz.isaom.models.MaleSelection
import com.ketchupzzz.isaom.models.sections.Sections
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.utils.Email
import com.ketchupzzz.isaom.utils.Fullname
import com.ketchupzzz.isaom.utils.Password

data class RegisterState(
    val isLoading : Boolean = false,
    val name : Fullname = Fullname(),
    val gradeLevel : String = "",
    val email: Email = Email(),

    val password: Password = Password(),
    val confirmPassword: Password = Password(),
    val isConfirmPasswordHidden : Boolean = false,
    val isPasswordHidden : Boolean = false,
    val section : Sections? = null,
    val sectionList : List<Sections> = emptyList(),
    val registerSuccess : Boolean = false,
    val errors : String ? = null,
    val userType: UserType ? = null,
    val genderSelection: GenderSelection = GenderSelection(
        males = MaleSelection(),
        females = FemaleSelection()
    ),
    val selectedGender : Gender ? = null,
    val selectedGenderUrl : String ? = null
)