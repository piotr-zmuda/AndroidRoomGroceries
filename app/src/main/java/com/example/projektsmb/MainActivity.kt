package com.example.projektsmb

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.projektsmb.ui.theme.ProjektSMBTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_databany.db").build()
    }

    private val viewModel by viewModels<ProductViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProductViewModel(db.productDao) as T
                }
            }
        }
    )
    private val viewModelCartWithProducts by viewModels<ShopingCartViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ShopingCartViewModel(db.cartDao,db.productDao,db.cartProdCrossRef) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjektSMBTheme {

                val navController = rememberNavController()

                val state by viewModel.state.collectAsState()
                val stateShopingCart by viewModelCartWithProducts.state.collectAsState()

                NavHost(navController, startDestination = "greeting_screen") {
                    composable("greeting_screen") {
                        Surface(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Greeting(name = "Android")
                                Button(onClick = { navController.navigate("product_screen") }) {
                                    Text(text = "Go to Product Screen")
                                }
                                Button(onClick = { navController.navigate("shoping_list_screen") }) {
                                    Text(text = "Go to Shoping List")
                                }
                                Button(onClick = { navController.navigate("shoping_cart_screen") }) {
                                    Text(text = "Go to all Carts")
                                }
                                Button(onClick = { setTheme(R.style.WhiteTheme);
                                    recreate() }) {
                                    Text(text = "White theme")
                                }
                                Button(onClick = { setTheme(R.style.BlackTheme);
                                    recreate() }) {
                                    Text(text = "Black Theme")
                                }
                            }
                        }
                    }
                    composable("product_screen") {
                        ProductScreen(state = state, onEvent = viewModel::onEvent)
                    }
                    composable("shoping_list_screen") {
                        ShopingCartScreen(state = stateShopingCart, viewModel=viewModelCartWithProducts,onEvent = viewModelCartWithProducts::onEvent, onBackClicked = {
                            navController.navigateUp() // Navigate back using NavHostController
                        })
                    }
                    composable("shoping_cart_screen") {
                        AllCartScreen(state = stateShopingCart, viewModel=viewModelCartWithProducts,onEvent = viewModelCartWithProducts::onEvent, onBackClicked = {
                            navController.navigateUp()
                        }, navController)
                    }
                    composable(
                        "cart_info_screen/{cartId}",
                        arguments = listOf(navArgument("cartId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val cartId = backStackEntry.arguments?.getString("cartId") ?: ""
                        val cart = stateShopingCart.shopingCarts.find { it.cartId.toString() == cartId }
                        CartInfo(
                            state = stateShopingCart,
                            viewModel = viewModelCartWithProducts,
                            onEvent = viewModelCartWithProducts::onEvent,
                            onBackClicked = {
                                navController.navigateUp()
                            }, cartProductCrossRefDao = db.cartProdCrossRef,cartId.toString()
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjektSMBTheme {
        Greeting("Android")
    }
}
