package com.example.projektsmb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projektsmb.ui.theme.ProductEvent
import com.example.projektsmb.ui.theme.ProductState
import com.example.projektsmb.ui.theme.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductToCart (viewModel: ShopingCartViewModel, onEvent: (ShopingCartEvent) -> Unit, modifier: Modifier = Modifier){
    // Fetch all products when the dialog opens

    val allProducts by viewModel.allProducts.collectAsState(initial = emptyList())
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ShopingCartEvent.HideDialog)
        },
        title = { Text(text = "Dodaj Produkt") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)){
                    items(allProducts){ product->
                        Row(modifier = Modifier.fillMaxWidth()){
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "${product.name}    ${product.price}", fontSize = 20.sp)
                            }
                            IconButton(onClick = { onEvent(ShopingCartEvent.AddProduct(product))}) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add this product to cart")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(ShopingCartEvent.HideDialog)
                }) {
                    Text(text = "Zamknij")
                }
            }
        })



}