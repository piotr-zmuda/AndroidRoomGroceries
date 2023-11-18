package com.example.projektsmb

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProductsWithCart(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "productId",
        entityColumn =  "cartId",
        associateBy = Junction(CartProductCrossRef::class)
    )
    val carts: List<ShopingCart>
)