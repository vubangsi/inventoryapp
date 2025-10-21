package com.mercel.inventoryapp.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.inventoryapp.data.datastore.UserPreferences
import com.mercel.inventoryapp.features.settings.uistate.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            userPreferences.username.collect { username ->
                _uiState.value = _uiState.value.copy(username = username)
            }
        }
    }
    
    fun logout(onLogoutComplete: () -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                userPreferences.logout()
                onLogoutComplete()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}