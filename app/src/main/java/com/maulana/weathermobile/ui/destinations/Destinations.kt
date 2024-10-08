package com.maulana.weathermobile.ui.destinations

import androidx.navigation.NavType
import androidx.navigation.navArgument


interface Destinations {
    val title: String
    val route: String
    val routeWithArgs: String
}

object HomeDestination : Destinations {
    override val title: String
        get() = "Dashboard"

    override val route: String
        get() = "home"

    override val routeWithArgs: String
        get() = route
}

object ProductDetailDestination : Destinations {
    override val title: String
        get() = "Transaction"

    override val route: String
        get() = "product_detail"

    override val routeWithArgs: String
        get() = "$route/{productId}"

    val arguments = listOf(
        navArgument(name = "productId") { type = NavType.StringType }
    )
}

object TransactionDestination : Destinations {
    override val title: String
        get() = "Transaction"

    override val route: String
        get() = "transaction"

    override val routeWithArgs: String
        get() = route
}

object AdjustmentDestination : Destinations {
    override val title: String
        get() = "Adjustment"

    override val route: String
        get() = "adjustment"

    override val routeWithArgs: String
        get() = "${AdjustmentDestination.route}/{productId}"

    val arguments = listOf(
        navArgument(name = "productId") { type = NavType.StringType }
    )
}

object OnBoardingDestination : Destinations {
    override val title: String
        get() = "OnBoarding"

    override val route: String
        get() = "onboarding"

    override val routeWithArgs: String
        get() = route
}

object NoteDestination : Destinations {
    override val title: String
        get() = "Notes"
    override val route: String
        get() = "notes"
    override val routeWithArgs: String
        get() = route

}

object InputDestination : Destinations {
    override val title: String
        get() = "Add"
    override val route: String
        get() = "add"
    override val routeWithArgs: String
        get() = route

}

object LoginDestination : Destinations {
    override val title: String
        get() = "Login"
    override val route: String
        get() = "login"
    override val routeWithArgs: String
        get() = route
}

object RegisterDestination : Destinations {
    override val title: String
        get() = "Register"
    override val route: String
        get() = "register"
    override val routeWithArgs: String
        get() = route

}

val destinations = listOf(
    HomeDestination,
    OnBoardingDestination,
    RegisterDestination,
    LoginDestination,
    NoteDestination, InputDestination,
    ProductDetailDestination,
    TransactionDestination, AdjustmentDestination
)
