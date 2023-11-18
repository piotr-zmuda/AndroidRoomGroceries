package com.example.projektsmb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    //suspend to stop program till query is finished
    //update or create
    //Flow pozwala nam na widzenie zmian
    @Upsert
    suspend fun upsertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM PRODUCT ORDER BY name ASC")
    fun getProductOrderByName(): Flow<List<Product>>

    @Query("SELECT * FROM PRODUCT ORDER BY price ASC")
    fun getProductOrderByPrice(): Flow<List<Product>>

}