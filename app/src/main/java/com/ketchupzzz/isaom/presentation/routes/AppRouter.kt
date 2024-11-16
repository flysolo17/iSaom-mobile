package com.ketchupzzz.isaom.presentation.routes

import com.google.gson.Gson
import com.ketchupzzz.isaom.models.SignLanguageLesson
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.models.games.Games
import com.ketchupzzz.isaom.models.subject.Subjects
import com.ketchupzzz.isaom.models.subject.activities.Activity
import com.ketchupzzz.isaom.models.subject.module.Modules
import com.ketchupzzz.isaom.presentation.main.teacher.subject.view_subject.modules.edit_module_content.data.ContentWithModuleID

import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class  AppRouter(val route : String) {
    //auth
    data object AuthRoutes : AppRouter(route = "auth")
    data object LoginScreen : AppRouter(route = "login")
    data object VerificationScreen : AppRouter(route = "verification")
    data object RegisterScreen : AppRouter(route = "register")
    data object ForgotPasswordScreen : AppRouter(route = "forgot-password")
    data object ChangePassword : AppRouter(route = "change-password")
    data object EditProfileRoute : AppRouter(route = "edit-profile/{args}") {
        fun navigate(args: Users) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "edit-profile/$encodedJson"
        }
    }

    data object GameRoute : AppRouter(route = "games")
    data object GamingRoute : AppRouter(route = "games/{args}") {
        fun navigate(args: Games) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "games/$encodedJson"
        }
    }

    data object SearchScreen : AppRouter(route = "search")

    data object HomeScreen : AppRouter(route = "home")
    object ProfileScreen : AppRouter(route = "profile")
    data object AboutScreen : AppRouter(route = "about")
    data object LeaderboardRoute : AppRouter(route = "leaderboard")
    data object TranslatorScreen : AppRouter(route = "translator")
    data object PrivacyPolicy : AppRouter(route = "privacy-policy")




    data object MainRoutes : AppRouter(route = "main")
    data object Dictionary: AppRouter(route = "dictionary")

    data object Lessons: AppRouter(route = "sign-language")
    data object ViewSignLanguageLessons : AppRouter(route = "sign-language/{args}") {
        fun navigate(args: SignLanguageLesson) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "sign-language/$encodedJson"
        }
    }
    data object TeacherDashboard : AppRouter(route = "Classes")

    //teacher
    data object CreateSubject : AppRouter(route = "create-subject")

    data object ViewSection : AppRouter(route = "view-section/{sectionID}") {
        fun createRoute(sectionID: String) = "view-section/$sectionID"
    }

    data object ViewSubject : AppRouter(route = "view-subject/{subjectID}") {
        fun createRoute(subjectID: String) = "view-subject/$subjectID"
    }

    data object EditSubject : AppRouter(route = "edit-subject/{id}") {
        fun createRoute(id: String) = "edit-subject/$id"
    }
    data object ViewModule : AppRouter(route = "view-module/{moduleID}") {
        fun createRoute(moduleID: String) = "view-module/$moduleID"
    }


    data object CreateModuleContent : AppRouter(route = "create-module-content/{moduleID}") {
        fun createRoute(moduleID: String) = "create-module-content/$moduleID"
    }

    data object EditModuleContent : AppRouter(route = "edit-module-content/{args}") {

        fun createRoute(args: ContentWithModuleID) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "edit-module-content/$encodedJson"
        }
    }


    //like this the title should be
    //View Activity
    data object ViewActivity : AppRouter(route = "view-activity/{activityID}") {
        fun createRoute(activityID: String) = "view-activity/$activityID"
    }



    //student

    data object StudentHomeScreen: AppRouter(route = "student-home")
    data object StudentViewSubject : AppRouter(route = "student-view-subject/{args}") {
        fun createRoute(args: Subjects) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "student-view-subject/$encodedJson"
        }
    }


    data object StudentViewModule : AppRouter(route = "student-view-module/{args}") {
        fun createRoute(args: Modules) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "student-view-module/$encodedJson"
        }
    }

    data object StudentViewActivity : AppRouter(route = "student-view-activity/{args}") {
        fun createRoute(args: Activity) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "student-view-activity/$encodedJson"
        }
    }




}