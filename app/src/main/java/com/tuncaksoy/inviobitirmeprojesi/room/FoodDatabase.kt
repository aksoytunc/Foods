package com.tuncaksoy.inviobitirmeprojesi.room

import androidx.room.*
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order

@Database(entities = [Food::class, Order::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun getFoodRoomDao(): FoodRoomDao
}