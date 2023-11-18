package com.example.projektsmb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShopingCart")
data class ShopingCart(
    @PrimaryKey(autoGenerate = true)
    val cartId:Long=0
)
