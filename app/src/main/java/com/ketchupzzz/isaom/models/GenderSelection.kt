package com.ketchupzzz.isaom.models

data class GenderSelection(
    val males : MaleSelection,
    val females : FemaleSelection
)


data class MaleSelection(
    val gender: Gender = Gender.MALE,
    val url : List<String> = emptyList()
)

data class FemaleSelection(
    val gender: Gender = Gender.FEMALE,
    val url : List<String> = emptyList()
)