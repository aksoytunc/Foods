package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    var foodRepository: FoodRepository,
    var firebaseAuth: FirebaseAuth
) : ViewModel() {
    val basketFoodList = MutableLiveData<List<Food>>()

    fun getModePreferences(): DisplayData = foodRepository.getModePreferences()

    fun getBasket() {
        CoroutineScope(Dispatchers.Main).launch {
            basketFoodList.value = foodRepository.getLastBasketList()
        }
    }
}
