package com.maulana.warehouse.domain

sealed class UIState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data object Init : UIState<Nothing>()
}