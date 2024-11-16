package com.ketchupzzz.isaom.repository.modules

import android.net.Uri
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.models.subject.module.ModuleWithContents
import com.ketchupzzz.isaom.models.subject.module.Modules

interface ModuleRepository {

   suspend fun getAllModules(subjectID : String , result : (UiState<List<Modules>>) -> Unit)
    suspend fun  createModule(modules: Modules,uri: Uri, result: (UiState<String>) -> Unit)


    suspend fun  createContent(moduleID : String, content: Content,uri : Uri ?, result: (UiState<String>) -> Unit)
    suspend fun deleteContent(moduleID: String, content: Content, result: (UiState<String>) -> Unit)
    suspend fun getModule(moduleID : String,result: (UiState<Modules?>) -> Unit)
    fun updateLock(moduleID: String,lock : Boolean,result: (UiState<String>) -> Unit)

    suspend fun getAllContents(moduleID : String,result: (UiState<List<Content>>) -> Unit)


    suspend fun deleteModule(
      modules: Modules,
      result: (UiState<String>) -> Unit
    )


}