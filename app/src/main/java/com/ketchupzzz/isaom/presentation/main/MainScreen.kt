package com.ketchupzzz.isaom.presentation.main

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.text.Text
import com.ketchupzzz.isaom.R
import com.ketchupzzz.isaom.models.UserType
import com.ketchupzzz.isaom.presentation.main.navigation.NavigationItems
import com.ketchupzzz.isaom.presentation.main.navigation.getNavItems
import com.ketchupzzz.isaom.presentation.routes.AppRouter
import com.ketchupzzz.isaom.presentation.routes.MainNavGraph
import com.ketchupzzz.isaom.utils.IsaomTopBar
import com.ketchupzzz.isaom.utils.ProgressBar
import com.ketchupzzz.isaom.utils.UnknownError
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    events: (MainEvents) -> Unit,
    mainNav: NavHostController,
    navHostController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items =   state.users.getNavItems()
    val isNavItem = items.any {
        it.route == currentRoute?.route
    }
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    when {
        state.isLoading -> ProgressBar(
            title = "Getting user..."
        )
        state.errors  != null-> UnknownError(
            title = state.errors
        )
        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.logo),
                                    modifier = modifier.size(100.dp),
                                    contentDescription = "Logo"
                                )
                                Text(
                                    "ISAOM",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Column {
                                items.forEachIndexed { index, item ->
                                    val isSelected = index == selectedIndex
                                    NavigationDrawerItem(
                                        label = {
                                            Text(item.label)
                                        },
                                        selected = isSelected,
                                        onClick = {
                                            selectedIndex = index
                                            navHostController.navigate(item.route) {
                                                popUpTo(navHostController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        }
                                    )
                                }
                            }

                            Column {
                                NavigationDrawerItem(
                                    label = {
                                        Text("Privacy Policy")
                                    },
                                    selected = false,
                                    icon = {
                                        Icon(imageVector = Icons.Default.PrivacyTip, contentDescription = "")
                                    },
                                    onClick = {

                                        navHostController.navigate(AppRouter.PrivacyPolicy.route) {
                                            popUpTo(navHostController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    }
                                )
                                Spacer(modifier.height(16.dp))
                                Text(
                                    "Version 1.0",
                                    modifier = modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.Gray
                                    )
                                )
                            }

                        }
                    }

                }
            ) {
                Scaffold(
                    topBar = {
                        if (currentRoute?.route != AppRouter.SearchScreen.route && currentRoute?.route  != AppRouter.GamingRoute.route) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                title = {
                                    if (!isNavItem) {
                                        val data = currentRoute?.route
                                        val title = data?.replace("-", " ")?.substringBefore("/{")?.split(" ")?.joinToString(" ") { it.capitalize(
                                            Locale.ROOT) } ?: ""
                                        Text(title)
                                    } else {
                                        val data = items[selectedIndex]
                                        Text(data.label)
                                    }
                                },
                                actions = {
                                    if (currentRoute?.route == AppRouter.Dictionary.route) {
                                        IconButton(
                                            colors = IconButtonDefaults.iconButtonColors(
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            onClick = {navHostController.navigate(AppRouter.SearchScreen.route)}
                                        ) {
                                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                                        }
                                    }

                                },
                                navigationIcon = {
                                    if (isNavItem) {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    } else {
                                        IconButton(
                                            onClick = {
                                                navHostController.popBackStack()
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                                        }
                                    }
                                }
                            )
                        }

                    }
                ) {
                    Box(
                        modifier = modifier.padding(it)
                    ) {
                        MainNavGraph(
                            users = state.users,
                            navHostController = navHostController,
                            mainNav = mainNav
                        )
                    }
                }
            }
        }
    }
}
