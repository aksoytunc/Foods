package com.tuncaksoy.inviobitirmeprojesi.listener

import com.tuncaksoy.inviobitirmeprojesi.data.model.Food

interface DetailsClickListener {
    fun addToBasketClick(foodNumber: String?)
    fun saveFavoriteClick(food: Food)
    fun plusClick()
    fun sourClick()
}