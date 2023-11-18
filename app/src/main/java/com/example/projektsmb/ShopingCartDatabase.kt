package com.example.projektsmb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShopingCart::class],
    version = 1
)
abstract class ShopingCartDatabase:RoomDatabase() {

    abstract val dao:ShopingCartDao


}