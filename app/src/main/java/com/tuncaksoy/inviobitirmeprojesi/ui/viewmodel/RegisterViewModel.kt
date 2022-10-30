package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.datasource.FoodDataSource
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import com.tuncaksoy.inviobitirmeprojesi.ui.view.LogoutActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    var foodRepository: FoodRepository,
    var foodDataSource: FoodDataSource
) : ViewModel() {

    fun register(
        activity: LogoutActivity,
        context: Context,
        userEmail: String,
        userPassword: String
    ) {
        foodRepository.register(activity, context, userEmail, userPassword)
    }
}