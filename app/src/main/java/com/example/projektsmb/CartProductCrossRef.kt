package com.example.projektsmb

import androidx.room.Entity

@Entity(primaryKeys = ["cartId", "productId"])
data class CartProductCrossRef(
    val cartId: Long,
    val productId:Long,
    val howMany:Int
)