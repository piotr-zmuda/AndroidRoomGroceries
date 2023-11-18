package com.example.projektsmb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projektsmb.ui.theme.ProductEvent
import com.example.projektsmb.ui.theme.ProductState
import com.example.projektsmb.ui.theme.SortType
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartInfo (
    state:ShopingCartState,
    viewModel: ShopingCartViewModel,
    onEvent: (ShopingCartEvent) -> Unit,
    onBackClicked: () -> Unit,
    cartProductCrossRefDao: CartProductCrossRefDao,
    cartId: String
){
    val cartProductCrossRefs by produceState<List<CartProductCrossRef>>(initialValue = emptyList()) {
        val cartProductCrossRefList = cartProductCrossRefDao.getCartProductCrossRefByCartId(cartId.toLong()).first()
        value = cartProductCrossRefList
    }
    Scaffold(
        topBar = {
            // TopAppBar with a back button
            TopAppBar(
                title = { Text(text = "Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = Modifier.padding(16.dp)
    ) {
            padding ->

        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)){
            items(cartProductCrossRefs) { cartProductCrossRef ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Id: ${cartProductCrossRef.productId}, Ilość: ${cartProductCrossRef.howMany}",
                                fontSize = 20.sp
                            )
                        }
                    }
            }
        }

    }

}