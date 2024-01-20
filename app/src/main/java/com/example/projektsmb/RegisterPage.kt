package com.example.projektsmb

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektsmb.ui.theme.ProductEvent
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(navController: NavController){
    var login by remember { mutableStateOf("Email") }
    var password by remember { mutableStateOf("Password") }
    var showErrorDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Label") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Label") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            val auth = FirebaseAuth.getInstance()
            println(login)
            println(password)
            auth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Login successful
                        val user = auth.currentUser
                        navController.navigate("greeting_screen")
                        // Handle successful login (e.g., navigate to next screen)
                    } else {
                        showErrorDialog = true
                    }
                }

        }) {
            Text(text = "Register")
        }

    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Register Failed") },
            text = { Text("Please check your credentials and try again.") },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("OK")
                }
            }
        )
    }
}