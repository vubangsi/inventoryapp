package com.mercel.inventoryapp.features.productdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.inventoryapp.data.model.InventoryItem
import com.mercel.inventoryapp.data.repository.InMemoryInventoryRepository
import com.mercel.inventoryapp.features.productdetail.uistate.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val inventoryRepository: InMemoryInventoryRepository,
    private val itemId: Int
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadItem()
    }
    
    private fun loadItem() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                val item = inventoryRepository.getItemById(itemId)
                if (item != null) {
                    _uiState.value = _uiState.value.copy(
                        item = item,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Item not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load item: ${e.message}"
                )
            }
        }
    }
    
    fun showEditDialog() {
        val item = _uiState.value.item ?: return
        _uiState.value = _uiState.value.copy(
            showEditDialog = true,
            editName = item.name,
            editPrice = item.price.toString(),
            editQuantity = item.quantity.toString()
        )
    }
    
    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(
            showEditDialog = false,
            editName = "",
            editPrice = "",
            editQuantity = ""
        )
    }
    
    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }
    
    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }
    
    fun updateEditName(name: String) {
        _uiState.value = _uiState.value.copy(editName = name)
    }
    
    fun updateEditPrice(price: String) {
        _uiState.value = _uiState.value.copy(editPrice = price)
    }
    
    fun updateEditQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(editQuantity = quantity)
    }
    
    fun updateItem(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val item = currentState.item ?: return
        
        if (currentState.editName.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Item name cannot be empty")
            return
        }
        
        val price = currentState.editPrice.toDoubleOrNull()
        if (price == null || price < 0) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a valid price")
            return
        }
        
        val quantity = currentState.editQuantity.toIntOrNull()
        if (quantity == null || quantity < 0) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a valid quantity")
            return
        }
        
        viewModelScope.launch {
            try {
                val updatedItem = item.copy(
                    name = currentState.editName,
                    price = price,
                    quantity = quantity
                )
                inventoryRepository.updateItem(updatedItem)
                _uiState.value = _uiState.value.copy(
                    item = updatedItem,
                    showEditDialog = false,
                    editName = "",
                    editPrice = "",
                    editQuantity = "",
                    errorMessage = null
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update item: ${e.message}"
                )
            }
        }
    }
    
    fun deleteItem(onSuccess: () -> Unit) {
        val item = _uiState.value.item ?: return
        
        viewModelScope.launch {
            try {
                inventoryRepository.deleteItem(item)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete item: ${e.message}",
                    showDeleteDialog = false
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}