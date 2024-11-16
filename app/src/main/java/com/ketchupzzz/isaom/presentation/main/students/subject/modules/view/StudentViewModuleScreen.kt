package com.ketchupzzz.isaom.presentation.main.students.subject.modules.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module.ContentWindow

import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.rememberHorizontalPdfReaderState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentViewModuleScreen(
    modifier: Modifier = Modifier,
    modules: Modules,
    state : StudentViewModuleScreenState,
    events : (StudentViewModuleScreenEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(
        modules
    ) {
        if (!modules.id.isNullOrEmpty()) {
            events.invoke(StudentViewModuleScreenEvents.OnGetContents(modules.id))
        }
    }
    when {
        state.isLoading -> ProgressBar(title = "Getting all contents")
        state.errors != null-> UnknownError(title =state.errors) {
            Button(onClick = {navHostController.popBackStack()}) {
                Text(text = "Back")
            }
        }
        state.module == null -> UnknownError(title = "No contents yet!")
        else -> {
            if (modules.content != null) {
                val pdfState = rememberHorizontalPdfReaderState(
                    resource = ResourceType.Remote(state.module.content!!),
                    isZoomEnable = true,
                )
                HorizontalPDFReader(
                    state = pdfState,
                    modifier = modifier
                        .fillMaxSize()
                )
            } else  {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    UnknownError(
                        title = "Invalid file"
                    ) {
                        Button(onClick = {navHostController.popBackStack()}) { Text("Back") }
                    }
                }
            }

        }
    }


}