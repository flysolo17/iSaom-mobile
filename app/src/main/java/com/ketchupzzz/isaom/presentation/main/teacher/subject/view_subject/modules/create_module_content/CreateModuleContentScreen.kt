package com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content

import android.widget.Toast

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Title

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateModuleContentScreen(
    modifier: Modifier = Modifier,
    moduleID : String,
    state : CreateModuleScreenState,
    events: (CreateModuleScreenEvents) -> Unit,
    navHostController: NavHostController
) {

    val textEditorState = rememberRichTextState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)

    ) {
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = {events.invoke(CreateModuleScreenEvents.OnTitleChange(it))},
            label = { Text(text = "Enter Title")},
            isError = false,
        )

        Spacer(modifier = modifier.height(16.dp))
        Text(text = "Content Body", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = modifier.height(8.dp))
        TextEditor(modifier = modifier, state = state ,textEditorState = textEditorState, events = events)
        Spacer(modifier = modifier.weight(1f))
        val context = LocalContext.current
        PrimaryButton(onClick = { events(
            CreateModuleScreenEvents.OnCreateContent(
                moduleID = moduleID,
                context = context,
                body = textEditorState.toHtml(),
                uri = state.uri,
                navHostController = navHostController
            )
        ) }, isLoading = state.isLoading) {
            Text(text = "Create Content")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditor(
    modifier: Modifier = Modifier,
    state: CreateModuleScreenState,
    textEditorState: RichTextState,
    events: (CreateModuleScreenEvents) -> Unit,
) {

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
    ) {
        TextEditorActions(textState = textEditorState,state = state, events = events)
        RichTextEditor(
            state = textEditorState,
            modifier = modifier.fillMaxWidth(),
            minLines = 4,
            colors = RichTextEditorDefaults.richTextEditorColors(
                focusedIndicatorColor = Color.Transparent,  // Remove underline when focused
                unfocusedIndicatorColor = Color.Transparent // Remove underline when not focused
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUrl(
    modifier: Modifier = Modifier,
    onSave : (title: String,url : String) -> Unit,
    sheetState : SheetState,
    onDismiss : () -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var url by remember {
        mutableStateOf("")
    }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Create Url", style = MaterialTheme.typography.titleLarge)
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = title,
                onValueChange = {title = it},
                label = { Text(text = "enter url name")}
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = url,
                onValueChange = {url = it},
                label = { Text(text = "enter url")}
            )
            PrimaryButton(onClick = {
                onSave(title,url)
            }) {
                Text(text = "Save")
            }
        }
    }


}
@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun TextEditorActions(
    modifier: Modifier = Modifier,
    textState: RichTextState,
    state: CreateModuleScreenState,
    events: (CreateModuleScreenEvents) -> Unit
) {
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    var bottomSheet by remember { mutableStateOf(false) }
    if (bottomSheet) {
        CreateUrl(
            onSave =  { t,u ->
               textState.addLink(text = t,u)
                bottomSheet = !bottomSheet
            },
            sheetState = sheetState,
            onDismiss =  {
                bottomSheet = !bottomSheet
                Toast.makeText(context,"cancelled", Toast.LENGTH_SHORT).show()
            }
        )
    }
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        columns = GridCells.Adaptive(36.dp),
        userScrollEnabled = false
    ) {
        item {
            ActionButtons(
                icon = Icons.Default.FormatBold,
                isActive = state.boldSelected,
                desc = "Bold Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.BOLD))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.FormatItalic,
                isActive = state.italicSelected,
                desc = "Italic Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.ITALIC))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.FormatUnderlined,
                isActive = state.underlineSelected,
                desc = "Underline Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.UNDERLINE))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.Title,
                isActive = state.titleSelected,
                desc = "Title Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(fontSize = titleSize))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.TITLE))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.FormatSize,
                isActive = state.subtitleSelected,
                desc = "Subtitle Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(fontSize = subtitleSize))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.SUBTITLE))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.FormatColorText,
                isActive = state.textColorSelected,
                desc = "Text Color Icon"
            ) {
                textState.toggleSpanStyle(SpanStyle(color = Color.Red))
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.TEXT_COLOR))
            }
        }
        item {
            ActionButtons(
                icon = Icons.Default.AddLink,
                desc = "Link Icon"
            ) {
                bottomSheet = true
            }
        }
        item {
            val alignmentIcon = when (state.alignments) {
                Alignments.START -> Icons.Default.FormatAlignLeft
                Alignments.CENTER -> Icons.Default.FormatAlignCenter
                Alignments.END -> Icons.Default.FormatAlignRight
                Alignments.JUSTIFY -> Icons.Default.FormatAlignJustify
            }

            ActionButtons(
                icon = alignmentIcon,
                isActive = true,
                desc = "Alignment Icon"
            ) {
                textState.toggleParagraphStyle(
                    when (state.alignments) {
                        Alignments.START -> ParagraphStyle(textAlign = TextAlign.Center)
                        Alignments.CENTER -> ParagraphStyle(textAlign = TextAlign.End)
                        Alignments.END -> ParagraphStyle(textAlign = TextAlign.Justify)
                        Alignments.JUSTIFY -> ParagraphStyle(textAlign = TextAlign.Start)
                    }
                )
                events.invoke(CreateModuleScreenEvents.OnActionButtonClicked(ActionButtons.ALIGNMENT))
            }
        }


        item {
            val listStyleIcon = when (state.listTypes) {
                ListTypes.NONE -> Icons.Default.FormatListBulleted // Default icon for no list
                ListTypes.UNORDERED -> Icons.Default.FormatListBulleted // Icon for unordered list
                ListTypes.ORDERED -> Icons.Default.FormatListNumbered // Icon for ordered list
            }
            val isActive = state.listTypes != ListTypes.NONE
            ActionButtons(
                icon = listStyleIcon,
                isActive = isActive,
                desc = "List Style Icon"
            ) {
                when (state.listTypes) {
                    ListTypes.NONE -> {
                        textState.addUnorderedList()
                        events.invoke(CreateModuleScreenEvents.OnListTypesSelected(ListTypes.UNORDERED)) // Default to unordered
                    }
                    ListTypes.UNORDERED -> {
                        textState.addOrderedList()
                        events.invoke(CreateModuleScreenEvents.OnListTypesSelected(ListTypes.ORDERED)) // Toggle to ordered
                    }
                    ListTypes.ORDERED -> {
                        events.invoke(CreateModuleScreenEvents.OnListTypesSelected(ListTypes.NONE)) // Toggle to none or clear
                    }
                }
            }

        }
    }
}





@Composable
fun ActionButtons(
    isActive : Boolean = false,
    icon : ImageVector,
    desc : String = "",
    action : () -> Unit
) {
    IconToggleButton(
        checked = isActive,
        onCheckedChange = { action() },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = desc,
            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}