package com.tuncaksoy.inviobitirmeprojesi.retrofit

class ApiUtils {
    companion object {
        var BASE_URL =
            "http://kasimadalan.pe.hu/"

        fun getFoodRetrofitDao(): FoodRetrofitDao =
            RetrofitClient.getClient(BASE_URL).create(FoodRetrofitDao::class.java)
    }
}