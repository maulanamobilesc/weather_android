package com.maulana.weathermobile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maulana.weathermobile.ui.destinations.HomeDestination
import com.maulana.weathermobile.ui.destinations.ManageLocationDestination
import com.maulana.weathermobile.ui.destinations.destinations
import com.maulana.weathermobile.ui.page.home.HomeScreen
import com.maulana.weathermobile.ui.page.managelocation.ManageLocationScreen


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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) }
    ) { innerPaddings ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            startDestination = HomeDestination.routeWithArgs
        ) {
            composable(HomeDestination.routeWithArgs) {
                HomeScreen(navController, snackBarState)
            }
            composable(ManageLocationDestination.routeWithArgs) {
                ManageLocationScreen(navController, snackBarState)
            }
        }
    }
}
