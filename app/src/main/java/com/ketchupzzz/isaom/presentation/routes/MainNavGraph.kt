package com.ketchupzzz.isaom.presentation.routes


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.ketchupzzz.isaom.models.SignLanguageLesson
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.presentation.auth.change_password.ChangePasswordScreen
import com.ketchupzzz.isaom.presentation.auth.change_password.ChangePasswordViewModel
import com.ketchupzzz.isaom.presentation.auth.edit_profile.EditProfileScreen
import com.ketchupzzz.isaom.presentation.auth.edit_profile.EditProfileViewModel
import com.ketchupzzz.isaom.presentation.main.about.AboutScreen

import com.ketchupzzz.isaom.presentation.main.teacher.dashboard.DashboardEvents
import com.ketchupzzz.isaom.presentation.main.teacher.dashboard.DashboardScreen
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryEvents
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryScreen
import com.ketchupzzz.isaom.presentation.main.dictionary.DictionaryViewModel
import com.ketchupzzz.isaom.presentation.main.home.HomeScreen

import com.ketchupzzz.isaom.presentation.main.lessons.LessonScreen
import com.ketchupzzz.isaom.presentation.main.lessons.LessonViewModel
import com.ketchupzzz.isaom.presentation.main.policy.PrivacyPolicyScreen
import com.ketchupzzz.isaom.presentation.main.profle.ProfileScreen
import com.ketchupzzz.isaom.presentation.main.profle.ProfileViewModel
import com.ketchupzzz.isaom.presentation.main.game.GameScreen
import com.ketchupzzz.isaom.presentation.main.game.GameViewModel
import com.ketchupzzz.isaom.presentation.main.gaming.GamingEvents
import com.ketchupzzz.isaom.presentation.main.gaming.GamingScreen
import com.ketchupzzz.isaom.presentation.main.gaming.GamingViewModel
import com.ketchupzzz.isaom.presentation.main.leaderboard.LeaderboardScreen
import com.ketchupzzz.isaom.presentation.main.leaderboard.LeaderboardViewModel
import com.ketchupzzz.isaom.presentation.main.lessons.view_lessons.ViewLessonScreen
import com.ketchupzzz.isaom.presentation.main.students.home.StudentHomeEvents
import com.ketchupzzz.isaom.presentation.main.students.home.StudentHomeScreen
import com.ketchupzzz.isaom.presentation.main.students.home.StudentHomeViewModel
import com.ketchupzzz.isaom.presentation.main.students.subject.activities.view.StudentViewActivity
import com.ketchupzzz.isaom.presentation.main.students.subject.activities.view.StudentViewActivityViewModel
import com.ketchupzzz.isaom.presentation.main.students.subject.modules.view.StudentViewModuleScreen
import com.ketchupzzz.isaom.presentation.main.students.subject.modules.view.StudentViewModuleViewModel
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectScreen
import com.ketchupzzz.isaom.presentation.main.students.subject.view_subject.StudentViewSubjectViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.dashboard.DashboardViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section.ViewSectionScreen
import com.ketchupzzz.isaom.presentation.main.teacher.sections.view_section.ViewSectionViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject.CreateSubjectScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.add_subject.SubjectViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject.EditSubjectScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.edit_subject.EditSubjectViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.ViewActivityScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.activities.view_activity.ViewActivityViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.ViewSubjectViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.CreateModuleContentScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.create_module_content.CreateModuleContentViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content.EditContentScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content.EditContentViewModel
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content.data.ContentWithModuleID
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module.ViewModuleScreen
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.view_module.ViewModuleViewModel
import com.ketchupzzz.isaom.presentation.main.translator.TranslatorScreen
import com.ketchupzzz.isaom.presentation.main.translator.TranslatorViewModel
import com.ketchupzzz.isaom.presentation.main.search.SearchScreen
import com.ketchupzzz.isaom.presentation.main.search.SearchScreenViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    mainNav : NavHostController,
    users: Users ? = null
) {
    NavHost(
        navController = navHostController,
        startDestination = AppRouter.HomeScreen.route
    ) {

        //student
        composable(
            route = AppRouter.GameRoute.route
        ) {
            val viewModel = hiltViewModel<GameViewModel>()
            GameScreen(state = viewModel.state, events = viewModel::events, navHostController = navHostController)
        }

        composable(
            route = AppRouter.GamingRoute.route,
        ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val games = Gson().fromJson(decodedJson, Games::class.java)
            val viewModel = hiltViewModel<GamingViewModel>()
            viewModel.events(GamingEvents.OnSetUsers(users))
            GamingScreen(
                games = games,
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(
            route = AppRouter.HomeScreen.route
        ) {
            HomeScreen(users = users, navHostController = navHostController)
        }

        composable(
            route = AppRouter.SearchScreen.route
        ) {
            val viewModel = hiltViewModel<SearchScreenViewModel>()
            SearchScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.AboutScreen.route
        ) {
            AboutScreen(navHostController = navHostController)
        }
        composable(
            route = AppRouter.PrivacyPolicy.route
        ) {
            PrivacyPolicyScreen(navHostController = navHostController)
        }

        composable(
            route = AppRouter.LeaderboardRoute.route
        ) {
            val viewModel = hiltViewModel<LeaderboardViewModel>()
            LeaderboardScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(route = AppRouter.TranslatorScreen.route) {
            val viewModel = hiltViewModel<TranslatorViewModel>()
            TranslatorScreen(state = viewModel.state, events = viewModel::events)
        }
        composable(
            route = AppRouter.Dictionary.route
        ) {
            val viewModel = hiltViewModel<DictionaryViewModel>()
            viewModel.events(DictionaryEvents.OnGetUsers(users))
            DictionaryScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(
            route = AppRouter.Lessons.route
        ) {
            val viewModel = hiltViewModel<LessonViewModel>()
            LessonScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.ViewSignLanguageLessons.route
        ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val signLanguage = Gson().fromJson(decodedJson, SignLanguageLesson::class.java)
            ViewLessonScreen(
                lesson = signLanguage,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.TeacherDashboard.route
        ) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            viewModel.events(DashboardEvents.OnSetUsers(users))
            DashboardScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(route = AppRouter.ProfileScreen.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                mainNav = mainNav,
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events
            )
        }
        composable(route = AppRouter.ChangePassword.route) {
            val viewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePasswordScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.EditProfileRoute.route,

            ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val users = Gson().fromJson(decodedJson, Users::class.java)
            val viewModel = hiltViewModel<EditProfileViewModel>()
            users?.let {
                EditProfileScreen(
                    users = it,
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController
                )
            }
        }

        composable(route = AppRouter.StudentHomeScreen.route) {
            val viewModel = hiltViewModel<StudentHomeViewModel>()
            viewModel.events(StudentHomeEvents.OnSetUsers(users))
            StudentHomeScreen(navHostController = navHostController, state = viewModel.state, events = viewModel::events)
        }

        composable(route = AppRouter.StudentViewSubject.route) {

            val args = it.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val content = Gson().fromJson(decodedJson, Subjects::class.java)
            val viewModel = hiltViewModel<StudentViewSubjectViewModel>()
            StudentViewSubjectScreen(
                subjectID = content.id ?: "",
                state = viewModel.state,
                event = viewModel::events,
                navHostController = navHostController,
            )
        }
        composable(route = AppRouter.StudentViewModule.route) {
            val args = it.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val content = Gson().fromJson(decodedJson, Modules::class.java)
            val viewModel = hiltViewModel<StudentViewModuleViewModel>()
            StudentViewModuleScreen(
                modules = content,
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
            )
        }

        composable(route = AppRouter.StudentViewActivity.route) {
            val args = it.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val content = Gson().fromJson(decodedJson, Activity::class.java)
            val viewModel = hiltViewModel<StudentViewActivityViewModel>()
            StudentViewActivity(
                activity = content,
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
            )
        }


        //teacher
        composable(route = AppRouter.CreateSubject.route) {
            val viewModel = hiltViewModel<SubjectViewModel>()
            CreateSubjectScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
            )
        }


        composable(route = AppRouter.ViewActivity.route) {
            val viewModel = hiltViewModel<ViewActivityViewModel>()
            val activityID = it.arguments?.getString("activityID") ?: ""
            ViewActivityScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                activityID = activityID,
            )
        }


        composable(route = AppRouter.ViewSubject.route) {
            val viewModel = hiltViewModel<ViewSubjectViewModel>()
            val subjectID = it.arguments?.getString("subjectID") ?: ""
            ViewSubjectScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                subjectID = subjectID,
            )
        }

        composable(route = AppRouter.EditSubject.route) {
            val viewModel = hiltViewModel<EditSubjectViewModel>()
            val subjectID = it.arguments?.getString("id") ?: ""
            EditSubjectScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                id = subjectID,
            )
        }


        composable(route = AppRouter.ViewModule.route) {
            val viewModel = hiltViewModel<ViewModuleViewModel>()
            val moduleID = it.arguments?.getString("moduleID") ?: ""
            ViewModuleScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                moduleID = moduleID,
            )
        }

        composable(route = AppRouter.CreateModuleContent.route) {
            val viewModel = hiltViewModel<CreateModuleContentViewModel>()
            val moduleID = it.arguments?.getString("moduleID") ?: ""
            CreateModuleContentScreen(
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events,
                moduleID = moduleID,
            )
        }

        composable(route = AppRouter.EditModuleContent.route) {
            val viewModel = hiltViewModel<EditContentViewModel>()
            val args = it.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val content = Gson().fromJson(decodedJson, ContentWithModuleID::class.java)
            EditContentScreen(
                content = content.content,
                moduleID = content.moduleID,
                navHostController = navHostController,
                state = viewModel.state,
                events = viewModel::events
            )
        }

        composable(route = AppRouter.ViewSection.route) {
            val viewModel = hiltViewModel<ViewSectionViewModel>()
            val sectionID = it.arguments?.getString("sectionID") ?: ""
            ViewSectionScreen(sectionID = sectionID, state = viewModel.state, events = viewModel::events, navHostController = navHostController)
        }

    }
}






