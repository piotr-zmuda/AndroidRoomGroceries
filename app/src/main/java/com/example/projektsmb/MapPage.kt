package com.example.projektsmb

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(navController: NavController) {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    val contextt = LocalContext.current
    var desc by remember { mutableStateOf("desc") }
    var name by remember { mutableStateOf("Name") }
    var range by remember { mutableStateOf("Name") }
    var latitudee by remember { mutableStateOf("") }
    var longLat by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
        )
        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = range,
            onValueChange = { range = it },
            label = { Text("Range") },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = {
            // Initialize Firebase
            val database = FirebaseDatabase.getInstance()

        // Get a reference to your node
                    val myRef: DatabaseReference = database.getReference("shops").push()

            myRef.child("name").setValue(name)
            myRef.child("desc").setValue(desc)
            myRef.child("range").setValue(range)
            myRef.child("lat").setValue(latitudee)
            myRef.child("longLat").setValue(longLat)




        }) {
            Text(text = "Add this shop")
        }
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    // Replace "YOUR_MAPBOX_ACCESS_TOKEN" with your actual Mapbox access token
                    Mapbox.getInstance(context, "pk.eyJ1IjoiczIxNTg4IiwiYSI6ImNscmtzMjNxYjA0MDMycXJhbnJwbTZ2eGIifQ.xWUVXIIhdW4bCcoAaYltPg")
                    onCreate(Bundle())
                    getMapAsync { mapboxMap ->
                        println("lalalaladida")
                        println(Manifest.permission.ACCESS_FINE_LOCATION)
                        println(ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED)
                        // Customize the map as needed
                        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                            // Map style has been set



                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                println("dabdabdab")
                            }else{

                                // Enable the user's location on the map
                                val locationComponent = mapboxMap.locationComponent


                                locationComponent.activateLocationComponent(context, it)


                                // Enable to make the user's location visible on the map
                                locationComponent.isLocationComponentEnabled = true

                                // Set the camera to follow the user's location
                                locationComponent.cameraMode = CameraMode.TRACKING

                                // Set the render mode for the location component
                                locationComponent.renderMode = RenderMode.NORMAL

                                locationComponent.apply {
                                    // ...
                                    setRenderMode(RenderMode.NORMAL)
                                    setLocationComponentEnabled(true)
                                }
                                println("GOOOOOD")
                                locationPermissionGranted = true

                                val lastKnownLocation = locationComponent.lastKnownLocation
                                val latitude = lastKnownLocation?.latitude
                                val longitude = lastKnownLocation?.longitude
                                val altitude = lastKnownLocation?.altitude
                                val bearing = lastKnownLocation?.bearing
                                val speed = lastKnownLocation?.speed

                                latitudee = latitude.toString()
                                longLat = longitude.toString()

                                println("Latitude: $latitude, Longitude: $longitude, Altitude: $altitude, Bearing: $bearing, Speed: $speed")



                                val database = FirebaseDatabase.getInstance().reference.child("shops")
                                val listener = object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (cartSnapshot in snapshot.children) {
                                            println(cartSnapshot.child("lat").toString())
                                            val marker = MarkerOptions()
                                                .setPosition(LatLng(cartSnapshot.child("lat").value.toString().toDouble(), cartSnapshot.child("longLat").value.toString().toDouble())) // Replace with your desired coordinates
                                                .setTitle(cartSnapshot.child("name").value.toString())

                                            mapboxMap.addMarker(marker)
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle onCancelled
                                    }
                                }
                                database.addListenerForSingleValueEvent(listener)

                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

    }



}
private fun requestLocationPermission(activity: Context) {
    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, permission)) {
        // Show an explanation to the user why the permission is needed
        // (optional, depending on your use case)
    } else {
        // No explanation needed, request the permission
        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_LOCATION_PERMISSION)
    }
}

private const val REQUEST_LOCATION_PERMISSION = 123
