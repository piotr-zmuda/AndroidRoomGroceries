package com.example.projektsmb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name:String,
    var price:Double,
    @PrimaryKey(autoGenerate = true)
    val productId:Long=0
)
