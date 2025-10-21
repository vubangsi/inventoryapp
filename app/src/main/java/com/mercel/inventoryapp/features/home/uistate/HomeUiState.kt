package com.mercel.inventoryapp.features.home.uistate

import com.mercel.inventoryapp.data.model.Joke

data class HomeUiState(
    val currentJoke: Joke? = null,
    val isLoadingJoke: Boolean = false,
    val jokeError: String? = null
)