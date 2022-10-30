package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.model.User
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    var foodRepository: FoodRepository,
    var firebaseAuth: FirebaseAuth
) : ViewModel() {
    val displayData = MutableLiveData<DisplayData>()
    var userLive = MutableLiveData<User>()

    fun getLiveUser() {
        userLive = foodRepository.getLiveUser()
    }

    fun updateBalance(balance: Int, loadBalance: Int) {
        val lastBalance = balance + loadBalance
        foodRepository.updateBalance(lastBalance)
    }

    fun updateImage(imageUrl: String) {
        foodRepository.updateImage(imageUrl)
    }

    fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            foodRepository.loadModePreferences(languageMode, displayMode)
        }
    }

    fun getModePrefences() {
        CoroutineScope(Dispatchers.Main).launch {
            displayData.value = foodRepository.getModePrefences()
        }
    }
}