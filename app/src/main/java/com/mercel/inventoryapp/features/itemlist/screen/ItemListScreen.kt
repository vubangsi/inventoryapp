package com.mercel.inventoryapp.features.itemlist.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mercel.inventoryapp.data.model.InventoryItem
import com.mercel.inventoryapp.data.model.ItemCategory
import com.mercel.inventoryapp.features.itemlist.viewmodel.ItemListViewModel
import com.mercel.inventoryapp.utils.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    category: ItemCategory,
    onItemClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ItemListViewModel = viewModel(factory = ViewModelFactory(LocalContext.current, category))
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::showAddDialog
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No items in ${category.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.items) { item ->
                            ItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (uiState.showAddDialog) {
        AddItemDialog(
            category = category,
            itemName = uiState.newItemName,
            itemPrice = uiState.newItemPrice,
            itemQuantity = uiState.newItemQuantity,
            onNameChange = viewModel::updateNewItemName,
            onPriceChange = viewModel::updateNewItemPrice,
            onQuantityChange = viewModel::updateNewItemQuantity,
            onConfirm = viewModel::addItem,
            onDismiss = viewModel::hideAddDialog,
            errorMessage = uiState.errorMessage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemCard(
    item: InventoryItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$${String.format("%.2f", item.price)} x ${item.quantity} = $${String.format("%.2f", item.price * item.quantity)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemDialog(
    category: ItemCategory,
    itemName: String,
    itemPrice: String,
    itemQuantity: String,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    errorMessage: String?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add item to ${category.name}") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = onPriceChange,
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = itemQuantity,
                    onValueChange = onQuantityChange,
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}