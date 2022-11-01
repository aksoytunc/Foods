package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    var foodRepository: FoodRepository
) : ViewModel() {
    var answer = MutableLiveData<Answer>()

    fun login(email: String, password: String){
        answer = foodRepository.login(email,password)
    }
}