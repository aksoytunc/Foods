package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.DisplayData
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(var foodRepository: FoodRepository) : ViewModel() {
    val displayData = MutableLiveData<DisplayData>()

    fun deleteUserIdPref() {
        CoroutineScope(Dispatchers.Main).launch {
            foodRepository.deleteUserIdPref()
        }
    }

    fun getModePrefences() {
        CoroutineScope(Dispatchers.Main).launch {
            displayData.value = foodRepository.getModePrefences()
        }
    }
}
