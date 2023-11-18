package com.example.projektsmb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductCrossRefDao {
    @Upsert
    suspend fun upsertCrossRef(crossRef: CartProductCrossRef)

    @Query("SELECT * FROM CartProductCrossRef")
    fun getCartProductCrossRefDao(): Flow<List<CartProductCrossRef>>

    @Query("SELECT * FROM CartProductCrossRef WHERE cartId = :cartId")
    fun getCartProductCrossRefByCartId(cartId: Long): Flow<List<CartProductCrossRef>>
}