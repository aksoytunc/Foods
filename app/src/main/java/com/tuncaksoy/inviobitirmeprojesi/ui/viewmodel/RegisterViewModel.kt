package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    var foodRepository: FoodRepository
) : ViewModel() {
    var answer = MutableLiveData<Answer>()

    fun register(userEmail: String, userPassword: String) {
        answer = foodRepository.register(userEmail, userPassword)
    }
}