package com.mercel.inventoryapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mercel.inventoryapp.data.datastore.UserPreferences
import com.mercel.inventoryapp.data.repository.InMemoryInventoryRepository
import com.mercel.inventoryapp.data.model.ItemCategory
import com.mercel.inventoryapp.features.home.viewmodel.HomeViewModel
import com.mercel.inventoryapp.features.itemlist.viewmodel.ItemListViewModel
import com.mercel.inventoryapp.features.login.viewmodel.LoginViewModel
import com.mercel.inventoryapp.features.productdetail.viewmodel.ProductDetailViewModel
import com.mercel.inventoryapp.features.settings.viewmodel.SettingsViewModel

class ViewModelFactory(
    private val context: Context,
    private val category: ItemCategory? = null,
    private val itemId: Int? = null
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(context) as T
            LoginViewModel::class.java -> LoginViewModel(UserPreferences(context)) as T
            SettingsViewModel::class.java -> SettingsViewModel(UserPreferences(context)) as T
            ItemListViewModel::class.java -> {
                require(category != null) { "Category required for ItemListViewModel" }
                ItemListViewModel(InMemoryInventoryRepository.getInstance(), category) as T
            }
            ProductDetailViewModel::class.java -> {
                require(itemId != null) { "ItemId required for ProductDetailViewModel" }
                ProductDetailViewModel(InMemoryInventoryRepository.getInstance(), itemId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}