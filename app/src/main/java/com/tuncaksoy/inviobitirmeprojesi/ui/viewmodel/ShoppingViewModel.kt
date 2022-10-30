package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.model.Order
import com.tuncaksoy.inviobitirmeprojesi.data.model.User
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(var foodRepository: FoodRepository) : ViewModel() {
    var basketFoodList = MutableLiveData<List<Food>>()
    var liveUser = MutableLiveData<User>()
    var answer = MutableLiveData<Answer>()

    fun getBasket() {
        CoroutineScope(Dispatchers.Main).launch {
            basketFoodList.value = foodRepository.getLastBasketList()
        }
    }

    fun deleteToBasket(foodBasketId: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            answer.value = foodRepository.deleteToBasket(foodBasketId)
        }
    }

    fun saveFavoritesFood(food: Food) {
        CoroutineScope(Dispatchers.Main).launch {
            foodRepository.saveFavoritesFood(food)
        }
    }

    fun saveOrder(order: Order) {
        CoroutineScope(Dispatchers.Main).launch {
            foodRepository.saveOrder(order)
        }
    }

    fun getLiveUser() {
        liveUser = foodRepository.getLiveUser()
    }

    fun updateBalance(balance: Int, deleteBalance: Int) {
        val lastBalance = balance - deleteBalance
        foodRepository.updateBalance(lastBalance)
    }
}