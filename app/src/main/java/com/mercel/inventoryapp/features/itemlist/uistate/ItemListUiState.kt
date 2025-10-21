package com.mercel.inventoryapp.features.itemlist.uistate

import com.mercel.inventoryapp.data.model.InventoryItem

data class ItemListUiState(
    val items: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingItem: InventoryItem? = null,
    val newItemName: String = "",
    val newItemPrice: String = "",
    val newItemQuantity: String = "",
    val editItemName: String = "",
    val editItemCategory: String = "",
    val editItemPrice: String = "",
    val editItemQuantity: String = ""
)