package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(var foodRepository: FoodRepository) : ViewModel() {
    val favoritesList = MutableLiveData<List<Food>>()
    val answer = MutableLiveData<Answer>()

    fun getAllFavorites() {
        CoroutineScope(Dispatchers.Main).launch {
            favoritesList.value = foodRepository.getFavoritesFood()
        }
    }

    fun deleteFavoritesFood(food: Food) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.deleteFavoritesFood(food)
        }
    }
}