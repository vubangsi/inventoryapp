package com.mercel.inventoryapp.features.productdetail.uistate

import com.mercel.inventoryapp.data.model.InventoryItem

data class ProductDetailUiState(
    val item: InventoryItem? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val editName: String = "",
    val editPrice: String = "",
    val editQuantity: String = ""
)