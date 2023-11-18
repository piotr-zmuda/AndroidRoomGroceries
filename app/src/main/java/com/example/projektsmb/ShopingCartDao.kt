package com.example.projektsmb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
sealed interface ShopingCartDao {
    @Upsert
    suspend fun upsertShopingCart(shopingCart: ShopingCart): Long

    @Query("SELECT * FROM ShopingCart")
    fun getShopingCarts(): Flow<List<ShopingCart>>
}