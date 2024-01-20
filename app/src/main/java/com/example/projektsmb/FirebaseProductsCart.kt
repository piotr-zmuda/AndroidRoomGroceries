package com.example.projektsmb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseProductsCart() {
    val database = FirebaseDatabase.getInstance()
    val cartsState = remember { mutableStateListOf<ProductClassFirebase>() }
    var selectedProduct = remember { mutableStateOf<ProductClassFirebase?>(null) }


    var newProductName by remember { mutableStateOf("") }
    var newProductPrice by remember { mutableStateOf("") }
    var newProductId by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    fun deleteProduct() {
        // Assuming you have a unique identifier for each product
        // Here we delete by the product ID, adjust this based on your data structure
        println(selectedProduct.value)
        val productId = selectedProduct.value?.cartId

        // Remove the product from Firebase using its ID
        if(productId!=null){
            val productReference = FirebaseDatabase.getInstance().reference.child("carts").child(productId)
            println(" THE CODE IS$productId")
            println(productReference)
            productReference.removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Deletion successful
                        println("Product deleted successfully")
                    } else {
                        // Deletion failed
                        println("Failed to delete product: ${task.exception}")
                    }
                }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {

                Button(
                    onClick = {
                        // Logic to add the new product to Firebase here
                        // Retrieve newProductName, newProductPrice, newProductId
                        // Add the new product using Firebase APIs
                        // After adding, reset the fields and hide the dialog
                        // Generate a unique key using Firebase push() method
                        val newCartReference = FirebaseDatabase.getInstance().reference.child("carts").push()

                        // Create a new cart object with generated key as the ID
                        val newCart = ProductClassFirebase(
                            newProductName,
                            newProductPrice.toDouble(),
                            System.currentTimeMillis() // Use timestamp as the ID
                        )

                        // Add the new cart to Firebase using the generated key
                        newCartReference.setValue(newCart)

                        showDialog = false
                        newProductName = ""
                        newProductPrice = ""
                        newProductId = ""
                    }
                ) {
                    Text("Add")
                }},
            text = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = newProductName,
                        onValueChange = { newProductName = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = newProductPrice,
                        onValueChange = { newProductPrice = it },
                        label = { Text("Product Price") },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = newProductId,
                        onValueChange = { newProductId = it },
                        label = { Text("Product ID") }
                    )
                }
            }
        )
    }
    if(showEditDialog){
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            confirmButton = {

                Button(
                    onClick = {
                        // Logic to add the new product to Firebase here
                        // Retrieve newProductName, newProductPrice, newProductId
                        // Add the new product using Firebase APIs
                        // After adding, reset the fields and hide the dialog
                        // Generate a unique key using Firebase push() method
                        println(selectedProduct)
                        println(selectedProduct.value?.productId)
                        if(selectedProduct.value?.cartId != null){
                            val newCartReference = FirebaseDatabase.getInstance().reference.child("carts").child(selectedProduct.value?.cartId.toString())

                            // Create a new cart object with generated key as the ID
                            val newCart = ProductClassFirebase(
                                newProductName,
                                newProductPrice.toDouble(),
                                newProductId.toLong()
                            )

                            // Add the new cart to Firebase using the generated key
                            newCartReference.setValue(newCart)

                            showDialog = false
                            newProductName = ""
                            newProductPrice = ""
                            newProductId = ""
                        }

                    }
                ) {
                    Text("Update")
                }},
            text = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextField(
                        value = newProductName,
                        onValueChange = { newProductName = it },
                        label = { Text(selectedProduct.value?.name.toString()) },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextField(
                        value = newProductPrice,
                        onValueChange = { newProductPrice = it },
                        label = { Text(selectedProduct.value?.price.toString()) },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextField(
                        value = newProductId,
                        onValueChange = { newProductId = it },
                        label = { Text(selectedProduct.value?.productId.toString()) }
                    )
                }
            }
        )
    }
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().reference.child("carts")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val carts = mutableListOf<ProductClassFirebase>()
                for (cartSnapshot in snapshot.children) {
                    val productTmp = ProductClassFirebase(
                        cartSnapshot.child("name").value.toString(),
                        cartSnapshot.child("price").value.toString().toDouble(),
                        cartSnapshot.child("productId").value.toString().toLong(),
                        cartSnapshot.key.toString()
                    )
                    carts.add(productTmp)
                }
                cartsState.clear()
                cartsState.addAll(carts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        }
        database.addValueEventListener(listener)
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(cartsState) { product ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    selectedProduct.value = product
                    showEditDialog = true
                }) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${product.productId}" + " ${product.name}" + "${product.price}",
                        fontSize = 20.sp
                    )
                    Button(
                        onClick = {
                            selectedProduct.value = product
                            newProductName = selectedProduct.value?.name.toString()
                            newProductPrice = selectedProduct.value?.price.toString()
                            newProductId = selectedProduct.value?.productId.toString()

                            showEditDialog = true },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Edit")
                    }
                    Button(
                        onClick = {
                            selectedProduct.value = product
                            deleteProduct()
                                  },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }

    Column (modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom){
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Product")
        }
    }

}
