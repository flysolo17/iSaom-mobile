package com.ketchupzzz.isaom.presentation.routes

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ketchupzzz.isaom.presentation.auth.forgotPassword.ForgotPasswordScreen
import com.ketchupzzz.isaom.presentation.auth.forgotPassword.ForgotPasswordViewModel
import com.ketchupzzz.isaom.presentation.auth.login.LoginScreen
import com.ketchupzzz.isaom.presentation.auth.login.LoginViewModel
import com.ketchupzzz.isaom.presentation.auth.register.RegisterScreen
import com.ketchupzzz.isaom.presentation.auth.register.RegisterViewModel
import com.ketchupzzz.isaom.presentation.auth.verification.VerificationScreen
import com.ketchupzzz.isaom.presentation.auth.verification.VerificationViewModel

fun NavGraphBuilder.authNavGraph(navHostController: NavHostController) {
    navigation(startDestination = AppRouter.LoginScreen.route,route = AppRouter.AuthRoutes.route) {
        composable(route = AppRouter.LoginScreen.route) {
            val viewmodel = hiltViewModel<LoginViewModel>()
            val state = viewmodel.state
            val events = viewmodel::onEvent
            LoginScreen(navHostController = navHostController, state = state, events = events)
        }

        composable(route = AppRouter.VerificationScreen.route) {
            val viewmodel = hiltViewModel<VerificationViewModel>()
            val state = viewmodel.state
            val events = viewmodel::events
            VerificationScreen(navHostController = navHostController, state = state, events = events)
        }
        composable(route = AppRouter.RegisterScreen.route) {
            val viewmodel = hiltViewModel<RegisterViewModel>()
            val state = viewmodel.state
            val events = viewmodel::onEvent
            RegisterScreen(navHostController = navHostController, state = state, events = events)
        }


        composable(route = AppRouter.ForgotPasswordScreen.route) {
            val viewModel = hiltViewModel<ForgotPasswordViewModel>()
            ForgotPasswordScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
    }
}