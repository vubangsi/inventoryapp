package com.mercel.inventoryapp.features.productdetail.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mercel.inventoryapp.data.repository.InMemoryInventoryRepository
import com.mercel.inventoryapp.features.productdetail.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    itemId: Int,
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel {
        val repository = InMemoryInventoryRepository()
        ProductDetailViewModel(repository, itemId)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.item?.name ?: "Item Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.item != null) {
                        IconButton(onClick = viewModel::showEditDialog) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = viewModel::showDeleteDialog) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
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
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(
                                onClick = onBackClick,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Go Back")
                            }
                        }
                    }
                }
                uiState.item != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = uiState.item!!.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = "This is a product in ${uiState.item!!.category.name.lowercase()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Divider()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Price:",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", uiState.item!!.price)}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Quantity:",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "${uiState.item!!.quantity}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total Value:",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", uiState.item!!.price * uiState.item!!.quantity)}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (uiState.showEditDialog) {
        EditItemDialog(
            itemName = uiState.editName,
            itemPrice = uiState.editPrice,
            itemQuantity = uiState.editQuantity,
            onNameChange = viewModel::updateEditName,
            onPriceChange = viewModel::updateEditPrice,
            onQuantityChange = viewModel::updateEditQuantity,
            onConfirm = { viewModel.updateItem {} },
            onDismiss = viewModel::hideEditDialog,
            errorMessage = uiState.errorMessage
        )
    }
    
    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteDialog,
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete this item? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteItem { onBackClick() } }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDeleteDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditItemDialog(
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
        title = { Text("Edit Item") },
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
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}