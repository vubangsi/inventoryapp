package com.mercel.inventoryapp.features.itemlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercel.inventoryapp.data.model.InventoryItem
import com.mercel.inventoryapp.data.model.ItemCategory
import com.mercel.inventoryapp.data.repository.InMemoryInventoryRepository
import com.mercel.inventoryapp.features.itemlist.uistate.ItemListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemListViewModel(
    private val inventoryRepository: InMemoryInventoryRepository,
    private val category: ItemCategory
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ItemListUiState())
    val uiState: StateFlow<ItemListUiState> = _uiState.asStateFlow()
    
    init {
        loadItems()
    }
    
    private fun loadItems() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            inventoryRepository.getItemsByCategory(category).collect { items ->
                _uiState.value = _uiState.value.copy(
                    items = items,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }
    
    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }
    
    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(
            showAddDialog = false,
            newItemName = "",
            newItemPrice = "",
            newItemQuantity = ""
        )
    }
    
    fun updateNewItemName(name: String) {
        _uiState.value = _uiState.value.copy(newItemName = name)
    }
    
    fun updateNewItemPrice(price: String) {
        _uiState.value = _uiState.value.copy(newItemPrice = price)
    }
    
    fun updateNewItemQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(newItemQuantity = quantity)
    }
    
    fun addItem() {
        val currentState = _uiState.value
        
        if (currentState.newItemName.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Item name cannot be empty")
            return
        }
        
        val price = currentState.newItemPrice.toDoubleOrNull()
        if (price == null || price < 0) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a valid price")
            return
        }
        
        val quantity = currentState.newItemQuantity.toIntOrNull()
        if (quantity == null || quantity < 0) {
            _uiState.value = currentState.copy(errorMessage = "Please enter a valid quantity")
            return
        }
        
        viewModelScope.launch {
            try {
                val newItem = InventoryItem(
                    name = currentState.newItemName,
                    category = category,
                    price = price,
                    quantity = quantity
                )
                inventoryRepository.insertItem(newItem)
                hideAddDialog()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add item: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}