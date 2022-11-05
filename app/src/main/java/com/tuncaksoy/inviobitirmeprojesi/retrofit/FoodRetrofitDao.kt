package com.tuncaksoy.inviobitirmeprojesi.retrofit

import com.tuncaksoy.inviobitirmeprojesi.data.model.Answer
import com.tuncaksoy.inviobitirmeprojesi.data.model.BasketFoodAnswer
import com.tuncaksoy.inviobitirmeprojesi.data.model.FoodAnswer
import retrofit2.http.*

interface FoodRetrofitDao {
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getAllFood(): FoodAnswer

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addToBasket(
        @Field("yemek_adi") foodName: String?,
        @Field("yemek_resim_adi") foodImage: String?,
        @Field("yemek_fiyat") foodPrice: Int?,
        @Field("yemek_siparis_adet") foodNumber: Int?,
        @Field("kullanici_adi") userEmail: String?
    ): Answer

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun getBasket(@Field("kullanici_adi") userEmail: String?): BasketFoodAnswer

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun deleteToBasket(
        @Field("sepet_yemek_id") foodNumber: Int?,
        @Field("kullanici_adi") userEmail: String?
    ): Answer
}