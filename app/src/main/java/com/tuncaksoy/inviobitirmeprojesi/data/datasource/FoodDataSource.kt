package com.tuncaksoy.inviobitirmeprojesi.data.datasource


import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.tuncaksoy.inviobitirmeprojesi.data.Preferences.AppPref
import com.tuncaksoy.inviobitirmeprojesi.data.model.*
import com.tuncaksoy.inviobitirmeprojesi.retrofit.FoodRetrofitDao
import com.tuncaksoy.inviobitirmeprojesi.room.FoodRoomDao
import com.tuncaksoy.inviobitirmeprojesi.utils.makeToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class FoodDataSource(
    var foodRetrofitDao: FoodRetrofitDao,
    var foodRoomDao: FoodRoomDao,
    var firebaseAuth: FirebaseAuth,
    var collectionReference: CollectionReference,
    var appPref: AppPref
) {
    var allFoodList = listOf<Food>()
    var basketFoodList = listOf<Food>()
    var newAllFoodList = listOf<Food>()
    var filterFoodList = listOf<Food>()
    var lastFoodList = listOf<Food>()
    var lastBasketList = listOf<Food>()
    var userLiveData = MutableLiveData<User>()
    var userAnswer = MutableLiveData<Answer>()
    var userId = ""
    var userEmail: String = "null"

    fun takeUserEmail() {
        userEmail = firebaseAuth.currentUser?.email.toString()
    }

    suspend fun getAllFood(): List<Food> {
        takeUserEmail()
        getUserIdPref()
        Log.e("userName", userEmail.toString())
        allFoodList = foodRetrofitDao.getAllFood().yemekler
        for (food in allFoodList) {
            food.kullanici_adi = userEmail
        }
        return allFoodList
    }

    suspend fun getBasket(): List<Food> {
        takeUserEmail()
        basketFoodList = try {
            foodRetrofitDao.getBasket(userEmail).sepet_yemekler
        } catch (e: java.io.EOFException) {
            emptyList()
        }
        return basketFoodList
    }

    suspend fun deleteBasketList(): List<Food> {
        basketFoodList = getBasket()
        if (basketFoodList.size > 1) {
            for (i in 0..basketFoodList.size - 2) {
                if (basketFoodList[i].yemek_adi == basketFoodList[i + 1].yemek_adi) {
                    Log.d("sildik", basketFoodList[i].yemek_adi.toString())
                    if (basketFoodList[i].yemek_siparis_adet?.toInt()!! >= basketFoodList[i + 1].yemek_siparis_adet?.toInt()!!) {
                        deleteToBasket(basketFoodList[i + 1].sepet_yemek_id)
                    } else deleteToBasket(basketFoodList[i].sepet_yemek_id)
                }
            }
        }
        return getBasket()
    }

    suspend fun getLastBasketList(): List<Food> {
        lastBasketList = deleteBasketList()
        for (favoriteFood in getFavoritesFood()) {
            for (basketFood in lastBasketList) {
                if (favoriteFood.yemek_adi == basketFood.yemek_adi) {
                    basketFood.food_id = favoriteFood.food_id
                    basketFood.yemek_favori = true
                }
            }
        }
        return lastBasketList
    }

    suspend fun getNewAllFood(): List<Food> {
        newAllFoodList = getAllFood()
        for (basketFood in deleteBasketList()) {
            for (newFood in newAllFoodList) {
                if (basketFood.yemek_adi == newFood.yemek_adi) {
                    newFood.yemek_sepet = true
                    newFood.sepet_yemek_id = basketFood.sepet_yemek_id
                    newFood.yemek_siparis_adet = basketFood.yemek_siparis_adet
                }
            }
        }
        return newAllFoodList
    }

    suspend fun getLastAllFood(): List<Food> {
        lastFoodList = getNewAllFood()
        for (favoriteFood in getFavoritesFood()) {
            for (lastFood in lastFoodList) {
                if (favoriteFood.yemek_adi == lastFood.yemek_adi) {
                    lastFood.food_id = favoriteFood.food_id
                    lastFood.yemek_favori = true
                }
            }
        }
        return lastFoodList
    }

    suspend fun filter(
        positionFilter: Int?,
        searchName: String,
        positionSort: Int?
    ): List<Food> {
        lastFoodList = getLastAllFood()
        when (positionFilter) {
            1 -> filterFoodList = lastFoodList.filter {
                it.yemek_adi.let {
                    it!!.contains("Izgara") || it.contains("Köfte") || it.contains("Lazanya") || it.contains(
                        "Makarna"
                    ) || it.contains("Pizza")
                }
            }
            2 -> filterFoodList = lastFoodList.filter {
                it.yemek_adi.let {
                    it!!.contains("Ayran") || it.contains("Su") || it.contains("Kahve") || it.contains(
                        "Fanta"
                    )
                }
            }
            3 -> filterFoodList = lastFoodList.filter {
                it.yemek_adi.let {
                    it!!.contains("Baklava") || it.contains("Kadayıf") || it.contains("Sütlaç") || it.contains(
                        "Tiramisu"
                    )
                }
            }
            else -> filterFoodList = lastFoodList
        }
        return searchFood(filterFoodList, searchName, positionSort)
    }

    fun searchFood(foodList: List<Food>, searchName: String, positionSort: Int?): List<Food> {
        val newSearchName = searchName.lowercase().replaceFirstChar {
            it.uppercase()
        }
        filterFoodList = foodList.filter {
            it.yemek_adi?.let {
                it.contains(newSearchName)
            } ?: false
        }
        return sort(filterFoodList, positionSort)
    }

    fun sort(foodList: List<Food>, position: Int?): List<Food> {
        when (position) {
            1 -> filterFoodList = foodList.sortedBy { it.yemek_adi }.reversed()
            2 -> filterFoodList = foodList.sortedBy { it.yemek_fiyat?.toDouble() }
            3 -> filterFoodList = foodList.sortedBy { it.yemek_fiyat?.toDouble() }.reversed()
            else -> filterFoodList = foodList.sortedBy { it.yemek_adi }
        }
        return filterFoodList
    }

    suspend fun addToBasket(
        foodName: String?,
        foodImage: String?,
        foodPrice: Int?,
        foodNumber: Int?
    ): Answer {
        takeUserEmail()
        return foodRetrofitDao.addToBasket(
            foodName,
            foodImage,
            foodPrice,
            foodNumber,
            userEmail
        )
    }


    suspend fun deleteToBasket(foodBasketId: Int?): Answer {
        takeUserEmail()
        return foodRetrofitDao.deleteToBasket(foodBasketId, userEmail)
    }

    suspend fun newProduct(product: Food): Food {
        basketFoodList = getBasket()
        var newProduct = product
        for (food in basketFoodList) {
            if (product.yemek_adi == food.yemek_adi) {
                newProduct = food
            }
        }
        return newProduct
    }

    suspend fun getFavoritesFood(): List<Food> {
        takeUserEmail()
        return foodRoomDao.getFavoritesFood(userEmail)
    }

    suspend fun saveFavoritesFood(food: Food): Answer {
        Log.e("2cevap geldi", food.toString())
        foodRoomDao.saveFavoritesFood(food)
        return Answer(1, "true")
    }

    suspend fun deleteFavoritesFood(food: Food): Answer {
        food.yemek_adi?.let { foodRoomDao.deleteFavoritesFood(it) }
        return Answer(1, "false")
    }

    suspend fun getOrder(): List<Order> {
        takeUserEmail()
        return foodRoomDao.getOrder(userEmail)
    }

    suspend fun saveOrder(order: Order): Answer {
        Log.e("liste", order.toString())
        foodRoomDao.saveOrder(order)
        return Answer(1, "true")
    }

    fun register(context: Context, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadUserFirebase(email)
                            } else makeToast(context, "Bilinmeyen bir hata oluştu !")
                        }
                }
            }.addOnFailureListener {
                makeToast(context, it.message.toString())
            }
    }

    fun loadUserFirebase(email: String?) {
        val newUser = User("", email, 0, "")
        collectionReference.add(newUser).addOnCompleteListener {
            it.addOnSuccessListener {
                val userId = HashMap<String, Any>()
                userId["userId"] = it.id
                collectionReference.document(it.id).update(userId)
                val answer = Answer(1, "Başarı ile Kayıt Olundu Giriş Yapılıyor")
                userAnswer.value = answer
            }
        }.addOnFailureListener {
            val answer = Answer(0, it.message)
            userAnswer.value = answer
            firebaseAuth.currentUser?.delete()
        }
    }

    fun getUserId() {
        takeUserEmail()
        collectionReference.get().addOnSuccessListener {
            for (user in it) {
                val hasmapp: HashMap<String, Any> = user.data as HashMap<String, Any>
                if (hasmapp.get("userEmail") == userEmail) {
                    CoroutineScope(Dispatchers.Main).launch {
                        userId = hasmapp.get("userId").toString()
                        appPref.loadUserPreferences(userId, userEmail)
                    }
                }
            }
        }
    }

    suspend fun getUserIdPref() {
        userId = appPref.getUserPreferences().userId.toString()
    }

    suspend fun deleteUserIdPref() = appPref.deleteUserPreferences()

    fun getLiveUser(): MutableLiveData<User> {
        collectionReference.document(userId).addSnapshotListener { value, error ->
            if (value != null) {
                val hasmapp: HashMap<String, Any?> = value.data as HashMap<String, Any?>
                val liveUser = User(
                    hasmapp.get("userId").toString(),
                    hasmapp.get("userEmail").toString(),
                    hasmapp.get("userBalance").toString().toInt(),
                    hasmapp.get("ppUrl").toString()
                )
                userLiveData.value = liveUser
            }
        }
        return userLiveData
    }

    fun updateBalance(lastBalance: Int) {
        val wallet = HashMap<String, Any>()
        wallet["userBalance"] = lastBalance
        collectionReference.document(userId).update(wallet)
    }

    fun updateImage(imageUrl: String) {
        val image = HashMap<String, Any>()
        image["ppUrl"] = imageUrl
        collectionReference.document(userId).update(image)
    }

    suspend fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) =
        appPref.loadModePreferences(languageMode, displayMode)

    suspend fun getModePrefences() = appPref.getModePrefences()
}
