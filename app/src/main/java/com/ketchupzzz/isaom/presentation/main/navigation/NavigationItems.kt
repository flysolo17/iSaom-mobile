package com.ketchupzzz.isaom.presentation.main.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.fadeIn
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


fun Users?.getNavItems() :List<NavigationItems> {
    if (this == null) {
        return listOf(
            NavigationItems(
                label = "Home",
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),

            NavigationItems(
                label = "About Ilocanos",
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
                label = "Home",
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),
            NavigationItems(
                label = "Leaderboard",
                selectedIcon = R.drawable.leaderboard_icon,
                unselectedIcon = R.drawable.leaderboard_icon,
                hasNews = false,
                route = AppRouter.LeaderboardRoute.route
            ),
            NavigationItems(
                label = "About Ilocanos",
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = AppRouter.AboutScreen.route
            ),


            NavigationItems(
                label = "Code Generator",
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = AppRouter.CreateSubject.route
            ),
            NavigationItems(
                label = "Profile",
                selectedIcon = R.drawable.user_filled,
                unselectedIcon = R.drawable.user_outlined,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            ),
        )
    } else{
        listOf(
            NavigationItems(
                label = "Home",
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),
            NavigationItems(
                label = "Leaderboard",
                selectedIcon = R.drawable.leaderboard_icon,
                unselectedIcon = R.drawable.leaderboard_icon,
                hasNews = false,
                route = AppRouter.LeaderboardRoute.route
            ),
            NavigationItems(
                label = "About Ilocanos",
                selectedIcon = R.drawable.about_filled,
                unselectedIcon = R.drawable.about_outline,
                hasNews = false,
                route = "about"
            ),
            NavigationItems(
                label = "Profile",
                selectedIcon = R.drawable.user_filled,
                unselectedIcon = R.drawable.user_outlined,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            ),
        )
    }
}