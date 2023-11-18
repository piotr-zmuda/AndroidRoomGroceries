package com.example.projektsmb

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CartWithProducts(
    @Embedded val shopingCart: ShopingCart,
    @Relation(
        parentColumn = "cartId",
        entityColumn =  "productId",
        associateBy = Junction(CartProductCrossRef::class)
    )
    val products: List<Product>
)