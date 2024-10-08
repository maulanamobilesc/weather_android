package com.maulana.weathermobile.domain

sealed class UserIntent {
    data object FetchItems : UserIntent()
    data object Logout : UserIntent()
}

sealed class LoginUserIntent : UserIntent() {
    data object Login : LoginUserIntent()
    data object ShowForgotPassword : LoginUserIntent()
    data object ShowSignUp : LoginUserIntent()
}