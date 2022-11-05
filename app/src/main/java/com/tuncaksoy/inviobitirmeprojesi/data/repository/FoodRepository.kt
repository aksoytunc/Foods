package com.tuncaksoy.inviobitirmeprojesi.data.repository

import com.tuncaksoy.inviobitirmeprojesi.data.datasource.FoodDataSource
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order

class FoodRepository(var foodDataSource: FoodDataSource) {

    suspend fun getNewAllFood() = foodDataSource.getLastAllFood()

    suspend fun getLastBasketList() = foodDataSource.getLastBasketList()

    suspend fun filter(positionFilter: Int?, searchName: String, positionSort: Int?) =
        foodDataSource.filter(positionFilter, searchName, positionSort)

    suspend fun addToBasket(
        foodName: String?, foodImage: String?, foodPrice: Int?, foodNumber: Int?
    ) = foodDataSource.addToBasket(foodName, foodImage, foodPrice, foodNumber)


    suspend fun deleteToBasket(foodBasketId: Int?) = foodDataSource.deleteToBasket(foodBasketId)

    suspend fun newProduct(product: Food) = foodDataSource.newProduct(product)

    suspend fun getFavoritesFood() = foodDataSource.getFavoritesFood()

    suspend fun saveFavoritesFood(food: Food) = foodDataSource.saveFavoritesFood(food)

    suspend fun deleteFavoritesFood(food: Food) = foodDataSource.deleteFavoritesFood(food)

    suspend fun getOrder(): List<Order> = foodDataSource.getOrder()

    suspend fun saveOrder(order: Order) = foodDataSource.saveOrder(order)

    fun login(email: String, password: String) = foodDataSource.login(email, password)

    fun register(userEmail: String, userPassword: String) =
        foodDataSource.register(userEmail, userPassword)

    fun getLiveUser() = foodDataSource.getLiveUser()

    fun updateBalance(lastBalance: Int) = foodDataSource.updateBalance(lastBalance)

    fun updateImage(imageUrl: String) = foodDataSource.updateImage(imageUrl)

    fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) =
        foodDataSource.loadModePreferences(languageMode, displayMode)

    fun getModePreferences() = foodDataSource.getModePreferences()
}