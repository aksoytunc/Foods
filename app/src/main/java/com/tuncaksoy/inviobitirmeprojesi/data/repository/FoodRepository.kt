package com.tuncaksoy.inviobitirmeprojesi.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tuncaksoy.inviobitirmeprojesi.data.datasource.FoodDataSource
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order
import com.tuncaksoy.inviobitirmeprojesi.data.model.User
import com.tuncaksoy.inviobitirmeprojesi.ui.view.LogoutActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository(var foodDataSource: FoodDataSource) {

    suspend fun getNewAllFood(): List<Food> =
        foodDataSource.getLastAllFood()

    suspend fun getLastBasketList(): List<Food> = foodDataSource.getLastBasketList()

    suspend fun filter(
        positionFilter: Int?,
        searchName: String,
        positionSort: Int?
    ): List<Food> =
        foodDataSource.filter(positionFilter, searchName, positionSort)

    suspend fun addToBasket(
        foodName: String?,
        foodImage: String?,
        foodPrice: Int?,
        foodNumber: Int?,
    ): Answer = withContext(Dispatchers.IO) {
        foodDataSource.addToBasket(
            foodName,
            foodImage,
            foodPrice,
            foodNumber,
        )
    }

    suspend fun deleteToBasket(foodBasketId: Int?): Answer =
        foodDataSource.deleteToBasket(foodBasketId)

    suspend fun newProduct(product: Food): Food = foodDataSource.newProduct(product)

    suspend fun getFavoritesFood(): List<Food> = foodDataSource.getFavoritesFood()

    suspend fun saveFavoritesFood(food: Food): Answer = foodDataSource.saveFavoritesFood(food)

    suspend fun deleteFavoritesFood(food: Food): Answer = foodDataSource.deleteFavoritesFood(food)

    suspend fun getOrder(): List<Order> = foodDataSource.getOrder()

    suspend fun saveOrder(order: Order): Answer = foodDataSource.saveOrder(order)

    fun register(
        activity: LogoutActivity,
        context: Context,
        userEmail: String,
        userPassword: String
    ) =
        foodDataSource.register(activity, context, userEmail, userPassword)

    fun getLiveUser(): MutableLiveData<User> = foodDataSource.getLiveUser()

    fun updateBalance(lastBalance: Int) = foodDataSource.updateBalance(lastBalance)

    fun updateImage(imageUrl: String) = foodDataSource.updateImage(imageUrl)

    fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) =
        foodDataSource.loadModePreferences(languageMode, displayMode)

    fun getModePreferences() = foodDataSource.getModePreferences()

}