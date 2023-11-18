package com.example.projektsmb.ui.theme

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projektsmb.CartProductCrossRef
import com.example.projektsmb.Product
import com.example.projektsmb.ProductDao
import com.example.projektsmb.ShopingCart

@Database(
    entities = [Product::class, ShopingCart::class, CartProductCrossRef::class],
    version = 1
)
abstract class ProductDatabase:RoomDatabase() {

    abstract val dao:ProductDao


}