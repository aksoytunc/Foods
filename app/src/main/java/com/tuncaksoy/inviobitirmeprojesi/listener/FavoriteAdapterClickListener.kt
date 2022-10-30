package com.tuncaksoy.inviobitirmeprojesi.listener

import android.view.View
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food

interface FavoriteAdapterClickListener {
    fun deleteFoodClick(food: Food)
    fun selectItemClick(view: View, food: Food)
}