package com.maulana.weathermobile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maulana.weathermobile.ui.page.home.HomeScreen
import com.maulana.weathermobile.ui.destinations.AdjustmentDestination
import com.maulana.weathermobile.ui.destinations.HomeDestination
import com.maulana.weathermobile.ui.destinations.LoginDestination
import com.maulana.weathermobile.ui.destinations.TransactionDestination
import com.maulana.weathermobile.ui.destinations.destinations


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val snackBarState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState()

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val currentScreen = destinations.find {
        backStackEntry?.destination?.route == it.route ||
                backStackEntry?.destination?.route == it.routeWithArgs || backStackEntry?.destination?.route.orEmpty()
            .startsWith(it.routeWithArgs)
    } ?: HomeDestination


    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedMenuItem by remember { mutableIntStateOf(0) }

    val topLevelRoutes = listOf(HomeDestination, TransactionDestination, AdjustmentDestination)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        bottomBar = {
            AnimatedVisibility(true) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination


                    topLevelRoutes.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selectedMenuItem == index,//currentDestination?.hierarchy?.any{it.hasRoute(item.route::class)} == true,
                            onClick = {

                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }

                                selectedMenuItem = index
                            }
                        )
                    }
                }
            }

        },
    ) { innerPaddings ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            startDestination = HomeDestination.routeWithArgs
        ) {
            composable(HomeDestination.routeWithArgs) {
                HomeScreen(navController, snackBarState)
                selectedMenuItem = 0
            }
        }
    }
}
