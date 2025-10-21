package com.mercel.inventoryapp.data.repository

import com.mercel.inventoryapp.data.model.InventoryItem
import com.mercel.inventoryapp.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryInventoryRepository {
    
    private val items = MutableStateFlow<List<InventoryItem>>(emptyList())
    private var nextId = 1
    
    fun getAllItems(): Flow<List<InventoryItem>> = items
    
    fun getItemsByCategory(category: ItemCategory): Flow<List<InventoryItem>> = 
        items.map { itemList -> itemList.filter { it.category == category } }
    
    suspend fun getItemById(id: Int): InventoryItem? = 
        items.value.find { it.id == id }
    
    suspend fun insertItem(item: InventoryItem) {
        val newItem = item.copy(id = nextId++)
        items.value = items.value + newItem
    }
    
    suspend fun updateItem(item: InventoryItem) {
        items.value = items.value.map { 
            if (it.id == item.id) item else it 
        }
    }
    
    suspend fun deleteItem(item: InventoryItem) {
        items.value = items.value.filter { it.id != item.id }
    }
    
    suspend fun deleteItemById(id: Int) {
        items.value = items.value.filter { it.id != id }
    }
}