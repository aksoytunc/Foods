package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import com.tuncaksoy.inviobitirmeprojesi.retrofit.NetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    var foodRepository: FoodRepository
) : ViewModel() {
    var lastFoodList = MutableLiveData<List<Food>>()
    var answer = MutableLiveData<Answer>()
    var searchNameView = ""

    fun getAllFood() {
        CoroutineScope(Dispatchers.Main).launch {
            lastFoodList.value = foodRepository.getNewAllFood()
        }
    }

    fun filter(positionFilter: Int?, searchName: String?, positionSort: Int?) {
        if (searchName != null) searchNameView = searchName
        CoroutineScope(Dispatchers.Main).launch {
            lastFoodList.value =
                foodRepository.filter(positionFilter, searchNameView, positionSort)
        }
    }

    fun addToBasket(foodName: String?, foodImage: String?, foodPrice: Int?, foodNumber: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.addToBasket(
                foodName,
                foodImage,
                foodPrice,
                foodNumber
            )
        }
    }

    fun deleteToBasket(foodBasketId: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.deleteToBasket(foodBasketId)
        }
    }

    fun saveFavoritesFood(food: Food) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.saveFavoritesFood(food)
        }
    }

    fun deleteFavoritesFood(food: Food) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.deleteFavoritesFood(food)
        }
    }
}