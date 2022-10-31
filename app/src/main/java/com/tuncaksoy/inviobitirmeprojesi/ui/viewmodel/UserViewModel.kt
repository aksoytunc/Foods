package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.model.User
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    var foodRepository: FoodRepository,
    var firebaseAuth: FirebaseAuth,
    var firebseStore: FirebaseStorage
) : ViewModel() {
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
        foodRepository.loadModePreferences(languageMode, displayMode)
        getModePrefences()
    }

    fun getModePrefences(): DisplayData = foodRepository.getModePreferences()

}