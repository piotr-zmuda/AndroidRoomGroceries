package com.example.projektsmb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projektsmb.ui.theme.ProductEvent
import com.example.projektsmb.ui.theme.ProductState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog (state:ProductState, onEvent: (ProductEvent) -> Unit, modifier: Modifier = Modifier){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ProductEvent.HideDialog)
        },
        title = { Text(text = "Dodaj Produkt") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(ProductEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Nazwa")
                    }
                )
                TextField(
                    value = state.price.toString(),
                    onValueChange = {
                        onEvent(ProductEvent.SetPrice(it.toDouble()))
                    },
                    placeholder = {
                        Text(text = "Cena")
                    }
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(ProductEvent.SaveProduct)
                }) {
                    Text(text = "Dodaj produkt")
                }
            }
        })



}