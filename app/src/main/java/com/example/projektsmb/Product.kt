package com.example.projektsmb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    val name:String,
    val price:Double,
    @PrimaryKey(autoGenerate = true)
    val productId:Long=0
)
