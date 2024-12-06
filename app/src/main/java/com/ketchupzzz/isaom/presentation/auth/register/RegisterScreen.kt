package com.ketchupzzz.isaom.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.Gender
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.presentation.auth.login.LoginEvents
import com.ketchupzzz.isaom.presentation.auth.register.account.AccountTypeScreen
import com.ketchupzzz.isaom.presentation.auth.register.gender.GenderScreen
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.ui.custom.PrimaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier =Modifier,
    navHostController: NavHostController,
    state: RegisterState,
    events: (RegisterEvents) -> Unit
) {

    val onBoardingState = rememberPagerState(0,0F) {
        3
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(state) {
        if (state.registerSuccess) {
            Toast.makeText(context,
                context.getString(R.string.successfully_created),Toast.LENGTH_SHORT).show()
            delay(1000L)
            navHostController.popBackStack()
        }
        if (state.errors !== null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }

    }
    Scaffold(
        topBar = { RegisterAppBar(
            state = onBoardingState,
            events = events,
            navHostController = navHostController
        )}
    ) {
        HorizontalPager(state = onBoardingState,modifier = modifier
            .fillMaxSize()
            .padding(it), userScrollEnabled = false) {page->
            when(page) {
                0 -> AccountTypeScreen() {
                    events(RegisterEvents.OnUserTypeSelected(it))
                    coroutineScope.launch {
                        onBoardingState.animateScrollToPage(onBoardingState.currentPage + 1)
                    }
                }
                1 -> GenderScreen(state = state, onSelected = { gender ,url ->
                    events(RegisterEvents.OnSelectGender(gender, url))
                    coroutineScope.launch {
                        onBoardingState.animateScrollToPage(onBoardingState.currentPage + 1)
                    }
                })
                2 -> RegisterLast(state = state, events = events, navHostController = navHostController)
                else -> AccountTypeScreen {
                    events(RegisterEvents.OnUserTypeSelected(it))
                    coroutineScope.launch {
                        onBoardingState.animateScrollToPage(onBoardingState.currentPage + 1)
                    }
                }
            }
        }
   }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RegisterAppBar(
    state : PagerState,
    events: (RegisterEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    fun appBarTitle(state: PagerState) : String{
        return  when(state.currentPage) {
            0 -> context.getString(R.string.select_account_type)
            1 -> context.getString(R.string.select_gender)
            2-> context.getString(R.string.registration_form)
            else -> context.getString(R.string.select_account_type)
        }
    }
    TopAppBar(
        title = {
            Text(
                text = appBarTitle(state),
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = {
                events(RegisterEvents.OnSelectGender(null, null))
                navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

        }
    )

}


@Composable
fun RegisterLast(
    modifier: Modifier = Modifier,
    state: RegisterState,
    events: (RegisterEvents) -> Unit,
    navHostController: NavHostController
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.register), style = MaterialTheme.typography.titleLarge)
        Text(text = stringResource(R.string.create_you_isaom_account), style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = modifier.weight(1f))
        RegisterForm(state = state, events = events)
        Spacer(modifier = modifier.weight(1f))
        TextButton(onClick = { navHostController.popBackStack() }) {
            Text(text = stringResource(R.string.already_have_an_account_sign_in_here))
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(modifier: Modifier = Modifier,state: RegisterState,events: (RegisterEvents) -> Unit) {

    Column(modifier = modifier
        .wrapContentSize()
        .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(R.string.required), color = MaterialTheme.colorScheme.error)
        OutlinedTextField(value = state.name.value, onValueChange = {
            events.invoke(RegisterEvents.OnNameChange(it))
        },
            modifier = modifier.fillMaxWidth(),
            isError = state.name.isError,
            label = {
                Text(text = stringResource(R.string.fullname))
            },
            supportingText = {
                Text(
                    text =   state.name.errorMessage ?: "" ,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(state.email.value,
            onValueChange = {
                events(RegisterEvents.OnEmailChange(it))
            },
            modifier = modifier.fillMaxWidth(),
            isError = state.email.isError,
            label = {
                Text(text = stringResource(R.string.email))
            },
            supportingText = {
                Text(
                    text =  state.email.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )








        if (state.userType == UserType.STUDENT) {
            val sections = state.sectionList
            var sectionExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = sectionExpanded, onExpandedChange =  {
                sectionExpanded = !sectionExpanded
            }) {
                OutlinedTextField(
                    readOnly = true,
                    value = state.section?.name ?: stringResource(R.string.no_section_selected),
                    onValueChange = { events(RegisterEvents.OnSectionChange(state.section)) },
                    label = { Text(stringResource(R.string.select_section)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectionExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = state.section == null,
                    supportingText = {
                        Text(
                            text = if( state.section == null ) stringResource(R.string.no_section_selected) else "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Start
                        )
                    },
                )

                ExposedDropdownMenu(
                    expanded = sectionExpanded,
                    onDismissRequest = { sectionExpanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    sections.forEach { section ->
                        DropdownMenuItem(
                            text = { Text(section.name?: stringResource(R.string.no_section)) },
                            onClick = {
                                events(RegisterEvents.OnSectionChange(section))
                                sectionExpanded = false
                            }
                        )
                    }
                }
            }
        }



        OutlinedTextField(
            value = state.password.value,
            maxLines = 1,
            isError = state.password.isError,

            onValueChange = {
                events(RegisterEvents.OnPasswordChange(it))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = {
                    events(RegisterEvents.OnTogglePasswordVisibility)
                }) {
                    Icon(
                        painter = painterResource(id = if (state.isPasswordHidden) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        contentDescription = if (state.isPasswordHidden) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (state.isPasswordHidden) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(R.string.password))
            },
            modifier = modifier.fillMaxWidth(),
            supportingText = {
                Text(text = state.password.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            } ,

            shape = RoundedCornerShape(8.dp),
            singleLine = true,

            )

        OutlinedTextField(
            value = state.confirmPassword.value,
            maxLines = 1,
            isError = state.confirmPassword.isError,

            onValueChange = {
                events(RegisterEvents.OnConfirmPasswordChange(it))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = {
                    events(RegisterEvents.OnToggleConfirmPasswordVisibility)
                }) {
                    Icon(
                        painter = painterResource(id = if (state.isConfirmPasswordHidden) R.drawable.ic_eye_off else R.drawable.ic_eye),
                        contentDescription = if (state.isConfirmPasswordHidden) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (state.isConfirmPasswordHidden) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(R.string.confirm_password))
            },
            modifier = modifier.fillMaxWidth(),
            supportingText = {
                Text(text = state.confirmPassword.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            } ,

            shape = RoundedCornerShape(8.dp),
            singleLine = true,

            )


        PrimaryButton(onClick = { events(RegisterEvents.OnCreateAccount) }, isLoading = state.isLoading) {
            Text(text = stringResource(R.string.register), fontWeight = FontWeight.Bold)
        }
    }
}