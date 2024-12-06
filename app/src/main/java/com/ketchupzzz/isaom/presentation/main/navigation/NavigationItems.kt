package com.ketchupzzz.isaom.presentation.main.navigation

import android.content.Context
import androidx.annotation.DrawableRes
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.models.Users
import com.ketchupzzz.isaom.presentation.routes.AppRouter

data class NavigationItems(
    val label : String,
    @DrawableRes val selectedIcon : Int,
    @DrawableRes val unselectedIcon : Int,
    val hasNews : Boolean,
    val badgeCount : Int? = null,
    val route : String
) {}


fun Users?.getNavItems(context : Context) :List<NavigationItems> {
    if (this == null) {
        return listOf(
            NavigationItems(
                label = context.getString(R.string.home),
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),

            NavigationItems(
                label = context.getString(R.string.about_ilocanos),
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = AppRouter.AboutScreen.route
            ),

        )
    }
    return if (this.type == UserType.TEACHER) {
        listOf(
            NavigationItems(
                label = context.getString(R.string.home),
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),
            NavigationItems(
                label = context.getString(R.string.leaderboard),
                selectedIcon = R.drawable.leaderboard_icon,
                unselectedIcon = R.drawable.leaderboard_icon,
                hasNews = false,
                route = AppRouter.LeaderboardRoute.route
            ),
            NavigationItems(
                label = context.getString(R.string.about_ilocanos),
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = AppRouter.AboutScreen.route
            ),


            NavigationItems(
                label = context.getString(R.string.code_generator),
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = AppRouter.CreateSubject.route
            ),
            NavigationItems(
                label = context.getString(R.string.profile),
                selectedIcon = R.drawable.user_filled,
                unselectedIcon = R.drawable.user_outlined,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            ),
        )
    } else{
        listOf(
            NavigationItems(
                label = context.getString(R.string.home),
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),
            NavigationItems(
                label = context.getString(R.string.leaderboard),
                selectedIcon = R.drawable.leaderboard_icon,
                unselectedIcon = R.drawable.leaderboard_icon,
                hasNews = false,
                route = AppRouter.LeaderboardRoute.route
            ),
            NavigationItems(
                label = context.getString(R.string.about_ilocanos),
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = "about"
            ),
            NavigationItems(
                label = context.getString(R.string.profile),
                selectedIcon = R.drawable.user_filled,
                unselectedIcon = R.drawable.user_outlined,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            ),
        )
    }
}