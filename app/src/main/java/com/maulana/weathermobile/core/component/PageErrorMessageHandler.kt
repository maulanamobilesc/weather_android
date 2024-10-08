package com.maulana.warehouse.core.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/*
@Composable
fun PageErrorMessageHandler(viewModel: BaseViewModel, snackbarHostState: SnackbarHostState) {
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Display Snackbar when an error message is emitted
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Dismiss"
            )
            viewModel.clearError() // Clear the error after showing the Snackbar
        }
    }
}*/

@Composable
fun PageErrorMessageHandler(errorMessage:String, snackbarHostState: SnackbarHostState) {
    // Display Snackbar when an error message is emitted
    LaunchedEffect(errorMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Dismiss"
            )
            //viewModel.clearError() // Clear the error after showing the Snackbar
        }
    }

