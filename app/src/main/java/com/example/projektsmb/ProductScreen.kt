package com.example.projektsmb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projektsmb.ui.theme.ProductEvent
import com.example.projektsmb.ui.theme.ProductState
import com.example.projektsmb.ui.theme.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen (
    state:ProductState,
    onEvent: (ProductEvent) -> Unit
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ProductEvent.ShowDialog)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new Product")
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        padding ->
        if(state.isAddingProduct){
            AddProductDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment =  CenterVertically
                ){
                    SortType.values().forEach { sortType ->  Row (
                        modifier = Modifier.clickable {
                            onEvent(ProductEvent.SortProduct(sortType))
                        },
                        verticalAlignment =CenterVertically
                    ) {
                        RadioButton(selected = state.sortType == sortType, onClick = {
                            onEvent(ProductEvent.SortProduct(sortType))
                        })
                        Text(text = sortType.name)
                    } }
                }
            }
            items(state.products){product->
                Row(modifier = Modifier.fillMaxWidth()){
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${product.name}    ${product.price}", fontSize = 20.sp)
                    }
                    IconButton(onClick = { onEvent(ProductEvent.DeleteProduct(product))}) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete this product")
                    }
                }
            }
    }

    }

}