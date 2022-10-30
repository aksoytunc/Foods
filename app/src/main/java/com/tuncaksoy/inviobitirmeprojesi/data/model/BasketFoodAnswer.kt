package com.tuncaksoy.inviobitirmeprojesi.data.model


data class BasketFoodAnswer(
    var sepet_yemekler: List<Food>,
    var success: Int?
) {
}