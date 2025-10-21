package com.mercel.inventoryapp.data.repository

import com.mercel.inventoryapp.data.model.InventoryItem
import com.mercel.inventoryapp.data.model.ItemCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryInventoryRepository private constructor() {
    
    private val items = MutableStateFlow<List<InventoryItem>>(createSampleData())
    private var nextId = 4 // Starting from 4 since we have 3 sample items
    
    companion object {
        @Volatile
        private var INSTANCE: InMemoryInventoryRepository? = null
        
        fun getInstance(): InMemoryInventoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = InMemoryInventoryRepository()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private fun createSampleData(): List<InventoryItem> {
        return listOf(
            InventoryItem(
                id = 1,
                name = "iPad",
                category = ItemCategory.ELECTRONICS,
                price = 950.0,
                quantity = 1
            ),
            InventoryItem(
                id = 2,
                name = "T-Shirt",
                category = ItemCategory.CLOTHING,
                price = 25.0,
                quantity = 5
            ),
            InventoryItem(
                id = 3,
                name = "Android Programming Book",
                category = ItemCategory.BOOKS,
                price = 45.0,
                quantity = 2
            )
        )
    }
    
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