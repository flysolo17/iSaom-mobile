package com.ketchupzzz.isaom.models.subject.module



data class ModuleWithContents(
    val modules: Modules ? =  null,
    val content: List<Content> = emptyList()
)