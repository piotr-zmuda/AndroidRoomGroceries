package com.example.projektsmb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class, ShopingCart::class, CartProductCrossRef::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val cartDao: ShopingCartDao
    abstract val cartProdCrossRef: CartProductCrossRefDao
}
