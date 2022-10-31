package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    var foodRepository: FoodRepository,
    var firebaseAuth: FirebaseAuth
) : ViewModel() {

    fun getModePreferences(): DisplayData = foodRepository.getModePreferences()
}
