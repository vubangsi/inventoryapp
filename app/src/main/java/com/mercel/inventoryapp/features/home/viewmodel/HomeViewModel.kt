package com.mercel.inventoryapp.features.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.mercel.inventoryapp.data.api.RetrofitClient
import com.mercel.inventoryapp.data.repository.JokeRepository
import com.mercel.inventoryapp.data.workmanager.JokeFetchWorker
import com.mercel.inventoryapp.features.home.uistate.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(private val context: Context) : ViewModel() {
    
    private val jokeRepository = JokeRepository(RetrofitClient.jokeApiService)
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        fetchJoke()
        schedulePeriodicJokeFetch()
    }
    
    fun fetchJoke() {
        _uiState.value = _uiState.value.copy(isLoadingJoke = true, jokeError = null)
        
        viewModelScope.launch {
            jokeRepository.getRandomJoke()
                .onSuccess { joke ->
                    _uiState.value = _uiState.value.copy(
                        currentJoke = joke,
                        isLoadingJoke = false,
                        jokeError = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingJoke = false,
                        jokeError = exception.message ?: "Failed to fetch joke"
                    )
                }
        }
    }
    
    private fun schedulePeriodicJokeFetch() {
        val workRequest = PeriodicWorkRequestBuilder<JokeFetchWorker>(30, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "joke_fetch_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}