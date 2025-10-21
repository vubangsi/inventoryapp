package com.mercel.inventoryapp.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.inventoryapp.data.datastore.UserPreferences
import com.mercel.inventoryapp.features.login.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }
    
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
    
    fun login() {
        val currentState = _uiState.value
        
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Please fill in all fields")
            return
        }
        
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                // Simple validation - any non-empty username/password combination works
                userPreferences.saveLoginState(true, currentState.username)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccessful = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Login failed: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}