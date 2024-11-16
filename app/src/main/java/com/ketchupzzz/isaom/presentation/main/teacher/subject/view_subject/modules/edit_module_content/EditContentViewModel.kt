package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.utils.UiState
import com.ketchupzzz.isaom.models.subject.module.Content
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.ActionButtons
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.Alignments
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.ListTypes
import com.ketchupzzz.isaom.repository.modules.ModuleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class EditContentViewModel @Inject constructor(
     private val moduleRepository: ModuleRepository
) : ViewModel() {
    var state by mutableStateOf(EditContentState())

    fun events(e : EditContentEvents) {
        when(e) {
            is EditContentEvents.OnActionButtonClicked -> updateButtons(e.actionButtons)
            is EditContentEvents.OnDescChange ->state = state.copy(desc = e.desc)
            is EditContentEvents.OnTitleChange -> state = state.copy(title = e.title)
            is EditContentEvents.OnListTypesSelected -> updateListTypes(e.types)
            is EditContentEvents.OnCreateContent -> createContent(e.moduleID,e.content,e.body,e.uri,e.context,e.navHostController)
            is EditContentEvents.OnImagePicked -> state  = state.copy(uri = e.uri)
            is EditContentEvents.OnInitializeContent -> initializeContent(e.content)
        }
    }

    private fun initializeContent(content: Content) {
            state = state.copy(
                title = content.title?: "",
                desc = content.desc ?: ""
            )
    }

    private fun createContent(
        moduleID: String,
        content: Content,
        body: String,
        uri: Uri?,
        context: Context,
        navHostController: NavHostController
    ) {
        val newContent = content.copy(
            title = state.title,
            desc = state.desc,
            body = body
        )
        viewModelScope.launch {
            moduleRepository.createContent(
                moduleID = moduleID,
                content = newContent,
                uri = uri
            ) {
                when(it) {
                    is UiState.Error -> {
                        Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                        state = state.copy(isLoading = false, errors = it.message)
                    }
                    UiState.Loading -> {state = state.copy(isLoading = true, errors = null)}
                    is UiState.Success ->  {
                        state = state.copy(isLoading = false, errors = null)
                        Toast.makeText(context,"Successfully Updated", Toast.LENGTH_SHORT).show()
                        navHostController.popBackStack()
                    }
                }
            }
        }
    }
    private fun updateListTypes(t: ListTypes) {
        state =  when(t) {
            ListTypes.NONE -> state.copy(listTypes = ListTypes.UNORDERED)
            ListTypes.UNORDERED -> state.copy(listTypes = ListTypes.ORDERED)
            ListTypes.ORDERED -> state.copy(listTypes = ListTypes.NONE)
        }
    }

    private fun updateButtons(b: ActionButtons) {
        state = when(b) {
            ActionButtons.BOLD -> state.copy(boldSelected = !state.boldSelected)
            ActionButtons.ITALIC -> state.copy(italicSelected = !state.italicSelected)
            ActionButtons.UNDERLINE -> state.copy(underlineSelected = !state.underlineSelected)
            ActionButtons.TITLE -> state.copy(titleSelected = !state.titleSelected)
            ActionButtons.SUBTITLE -> state.copy(subtitleSelected = !state.subtitleSelected)
            ActionButtons.TEXT_COLOR -> state.copy(textColorSelected = !state.textColorSelected)
            ActionButtons.LINK -> state.copy(linkSelected = !state.linkSelected)
            ActionButtons.ALIGNMENT -> {
                when (state.alignments) {
                    Alignments.START -> state.copy(alignments = Alignments.CENTER)
                    Alignments.CENTER -> state.copy(alignments = Alignments.END)
                    Alignments.END -> state.copy(alignments = Alignments.JUSTIFY)
                    Alignments.JUSTIFY -> state.copy(alignments = Alignments.START)
                    else -> state.copy(alignments = Alignments.START)
                }
            }
        }
    }
}