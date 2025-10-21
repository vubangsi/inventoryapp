package com.mercel.inventoryapp.data.model

data class InventoryItem(
    val id: Int = 0,
    val name: String,
    val category: ItemCategory,
    val price: Double,
    val quantity: Int
)

enum class ItemCategory {
    ELECTRONICS,
    CLOTHING,
    BOOKS
}