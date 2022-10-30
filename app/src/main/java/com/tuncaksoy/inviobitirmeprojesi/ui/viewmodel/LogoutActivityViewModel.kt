package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogoutActivityViewModel @Inject constructor(var foodRepository: FoodRepository) : ViewModel() {

    fun getUserId() {
        foodRepository.getUserId()
    }
}