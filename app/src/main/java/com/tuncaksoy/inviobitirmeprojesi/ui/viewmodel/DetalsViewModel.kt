package com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.Food
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalsViewModel @Inject constructor(var foodRepository: FoodRepository) : ViewModel() {
    var deleteAnswer = MutableLiveData<Answer>()
    var addAnswer = MutableLiveData<Answer>()
    var newProduct = MutableLiveData<Food>()

    fun deleteToBasket(product: Food, foodNumer: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            if (product.sepet_yemek_id != null) {
                deleteAnswer.value =
                    foodRepository.deleteToBasket(product.sepet_yemek_id)
            } else {
                addBasket(product, foodNumer)
            }
        }
    }

    fun addBasket(product: Food, foodNumer: Int?) {
        CoroutineScope(Dispatchers.Main).launch {
            addAnswer.value = foodRepository.addToBasket(
                product.yemek_adi,
                product.yemek_resim_adi,
                product.yemek_fiyat,
                foodNumer
            )
        }
    }

    fun newProduct(product: Food) {
        CoroutineScope(Dispatchers.Main).launch {
            newProduct.value = foodRepository.newProduct(product)
        }
    }
}

