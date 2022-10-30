package com.tuncaksoy.inviobitirmeprojesi.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order

@Dao
interface FoodRoomDao {
    //Food
    @Query("SELECT * FROM foods_fav WHERE kullanici_adi = :userEmail")
    suspend fun getFavoritesFood(userEmail: String): List<Food>

    @Insert
    suspend fun saveFavoritesFood(food: Food)

    @Query("DELETE FROM foods_fav WHERE yemek_adi = :foodName")
    suspend fun deleteFavoritesFood(foodName: String)

    //Order
    @Query("SELECT * FROM order_all WHERE user_email = :userEmail")
    suspend fun getOrder(userEmail: String): List<Order>

    @Insert
    suspend fun saveOrder(order: Order)

    @Query("DELETE FROM order_all WHERE order_id = :orderId")
    suspend fun deleteOrder(orderId: Int)
}