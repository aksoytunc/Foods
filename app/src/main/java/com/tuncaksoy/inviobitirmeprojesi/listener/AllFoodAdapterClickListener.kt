package com.tuncaksoy.inviobitirmeprojesi.listener

import android.view.View
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food

interface AllFoodAdapterClickListener {
    fun addToBasketClickListener(food: Food)
    fun selectItemClickListener(view: View, food: Food)
    fun saveFavoritesFoodClickListener(food: Food)
}