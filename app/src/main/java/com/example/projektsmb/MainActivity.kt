package com.example.projektsmb

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.projektsmb.ui.theme.ProjektSMBTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.mapboxsdk.Mapbox


//import com.example.service.MyBackgroundServic

class MainActivity : ComponentActivity() {

    //private val productReceiver = ProductReceiver()

    private val db by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_databany.db").build()
    }
    private var locationEngine: LocationEngine? = null

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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        val databaseReference = FirebaseDatabase.getInstance()
// Initialize Firebase
        val database = FirebaseDatabase.getInstance()

// Get a reference to your node
        val myRef: DatabaseReference = database.getReference("child")

// Initialize Firebase Auth
        val auth = FirebaseAuth.getInstance()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                // 'value' will contain the string "123" if it exists
                // Handle the value here
                println("FIREBASE VALUE "+ value)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur during the read operation
                println("ERROR FIREBASE")
            }
        })

        createChannel()
        //startBackgroundService()
        setContent {
            ProjektSMBTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                println("PERMISSION")
                val hasPermission = NotificationManagerCompat.from(context).areNotificationsEnabled()
                val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
                Mapbox.getInstance(this,"pk.eyJ1IjoiczIxNTg4IiwiYSI6ImNscmwxdHJwcDBtaGoya2swYjh4NjAydTUifQ.ZP-6tKICH6mz2cvrRvbA8Q");
                println("Notification Permission Status: $hasPermission")

                if (ContextCompat.checkSelfPermission(
                        this,
                        notificationPermission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(notificationPermission),
                        1001 // You need to define this constant
                    )
                } else {
                    // Permission has already been granted
                    println("Notification Permission Status: true")
                }

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    println("Notification Permission Status: true")
                }else{
                    locationEngine = LocationEngineProvider.getBestLocationEngine(this)
                    if (locationEngine != null) {
                        println("LOCATION ENGINE UP")
                    } else {
                        println("LOCATION ENGINE DOWN")
                    }

                    val request = LocationEngineRequest.Builder(1000L)
                        .setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setMaxWaitTime(5000L)
                        .build()


                    val callback = LocationCallback()

                    locationEngine?.requestLocationUpdates(request, callback, mainLooper)
                    locationEngine?.getLastLocation(callback)
                }



                val state by viewModel.state.collectAsState()
                val stateShopingCart by viewModelCartWithProducts.state.collectAsState()

                /*login_screen*/
                NavHost(navController, startDestination = "login_screen") {
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
                                Button(onClick = { navController.navigate("firebasedatabase_screen") }) {
                                    Text(text = "Go to Firebase Product Screen")
                                }
                                Button(onClick = { navController.navigate("product_screen") }) {
                                    Text(text = "Go to Product Screen")
                                }
                                Button(onClick = { navController.navigate("shoping_list_screen") }) {
                                    Text(text = "Go to Shoping List")
                                }
                                Button(onClick = { navController.navigate("shoping_cart_screen") }) {
                                    Text(text = "Go to all Carts")
                                }
                                Button(onClick = { navController.navigate("map_screen") }) {
                                    Text(text = "Mapa")
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
                    composable("map_screen") {
                        MapPage(navController = navController)
                    }
                    composable("firebasedatabase_screen") {
                        FirebaseProductsCart()
                    }
                    composable("product_screen") {
                        ProductScreen(context, state = state, onEvent = viewModel::onEvent, navController)
                    }
                    composable("login_screen") {
                        LoginPage(navController)
                    }
                    composable("register_screen") {
                        RegisterPage(navController)
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
                   /* composable(
                        "product_edit_screen/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId") ?: ""
                        val product = state.products.find { it.productId.toString() == productId }
                        if (product != null) {
                            EditProductDialog(context = context, product = product, onEvent = viewModel::onEvent, navController)
                        }
                    }*/
                }

            }
        }
    }
    /*override fun onStart() {
        super.onStart()
        registerReceiver(productReceiver, IntentFilter())
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(productReceiver)
    }*/
   /* private fun startBackgroundService() {
        //val serviceIntent = Intent(this, MyBackgroundServic::class.java)
        startService(serviceIntent)
    }*/
    private inner class LocationCallback : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult?) {
            result?.lastLocation?.let {
                val latitude = it.latitude
                // Use the latitude as needed
               println("LATIIIII "+latitude)
            }
        }

        override fun onFailure(exception: Exception) {
            // Handle failure
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(){
        val channel = NotificationChannel(
            getString(R.string.newProductChannel),
            "Channel for product notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        NotificationManagerCompat.from(applicationContext)
            .createNotificationChannel(channel)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
private fun initializeLocationEngine() {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjektSMBTheme {
        Greeting("Android")
    }
}
