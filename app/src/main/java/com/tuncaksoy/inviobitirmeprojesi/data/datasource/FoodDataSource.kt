package com.tuncaksoy.inviobitirmeprojesi.data.datasource


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.tuncaksoy.inviobitirmeprojesi.data.preference.AppSharedPreferences
import com.tuncaksoy.inviobitirmeprojesi.data.model.*
import com.tuncaksoy.inviobitirmeprojesi.retrofit.FoodRetrofitDao
import com.tuncaksoy.inviobitirmeprojesi.room.FoodRoomDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodDataSource(
    var foodRetrofitDao: FoodRetrofitDao,
    var foodRoomDao: FoodRoomDao,
    var firebaseAuth: FirebaseAuth,
    var collectionReference: CollectionReference,
    var appSharedPref: AppSharedPreferences,
) {
    var allFoodList = listOf<Food>()
    var basketFoodList = listOf<Food>()
    var newAllFoodList = listOf<Food>()
    var filterFoodList = listOf<Food>()
    var lastFoodList = listOf<Food>()
    var lastBasketList = listOf<Food>()
    val answerRegister = MutableLiveData<Answer>()
    val answerLogin = MutableLiveData<Answer>()
    var userLiveData = MutableLiveData<User>()
    var userId: String? = firebaseAuth.currentUser?.uid
    var userEmail: String? = firebaseAuth.currentUser?.email

    //Food
    suspend fun getAllFood(): List<Food> {
        userEmail = firebaseAuth.currentUser?.email
        userEmail?.let {
            allFoodList = foodRetrofitDao.getAllFood().yemekler
            for (food in allFoodList) {
                food.kullanici_adi = userEmail
            }
        }
        return allFoodList
    }

    suspend fun getBasket(): List<Food> {
        userEmail = firebaseAuth.currentUser?.email
        userEmail?.let {
            basketFoodList = try {
                foodRetrofitDao.getBasket(userEmail).sepet_yemekler
            } catch (e: java.io.EOFException) {
                emptyList()
            }
        }
        return basketFoodList
    }

    suspend fun deleteBasketList(): List<Food> {
        basketFoodList = getBasket()
        if (basketFoodList.size > 1) {
            for (i in 0..basketFoodList.size - 2) {
                if (basketFoodList[i].yemek_adi == basketFoodList[i + 1].yemek_adi) {
                    if (basketFoodList[i].yemek_siparis_adet?.toInt()!! >=
                        basketFoodList[i + 1].yemek_siparis_adet?.toInt()!!
                    ) {
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
                    it!!.contains("Izgara") || it.contains("Köfte") ||
                            it.contains("Lazanya") || it.contains(
                        "Makarna"
                    ) || it.contains("Pizza")
                }
            }
            2 -> filterFoodList = lastFoodList.filter {
                it.yemek_adi.let {
                    it!!.contains("Ayran") || it.contains("Su") ||
                            it.contains("Kahve") || it.contains("Fanta")
                }
            }
            3 -> filterFoodList = lastFoodList.filter {
                it.yemek_adi.let {
                    it!!.contains("Baklava") || it.contains("Kadayıf") ||
                            it.contains("Sütlaç") || it.contains("Tiramisu")
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
        filterFoodList = when (position) {
            1 -> foodList.sortedBy { it.yemek_adi }.reversed()
            2 -> foodList.sortedBy { it.yemek_fiyat?.toDouble() }
            3 -> foodList.sortedBy { it.yemek_fiyat?.toDouble() }.reversed()
            else -> foodList.sortedBy { it.yemek_adi }
        }
        return filterFoodList
    }

    suspend fun addToBasket(
        foodName: String?, foodImage: String?, foodPrice: Int?, foodNumber: Int?
    ): Answer = withContext(Dispatchers.IO) {
        foodRetrofitDao.addToBasket(foodName, foodImage, foodPrice, foodNumber, userEmail)
    }

    suspend fun deleteToBasket(foodBasketId: Int?): Answer =
        foodRetrofitDao.deleteToBasket(foodBasketId, userEmail)

    suspend fun newProduct(product: Food): Food {
        lastFoodList = getLastAllFood()
        var newProduct = product
        for (food in lastFoodList) {
            if (product.yemek_adi == food.yemek_adi) {
                newProduct = food
            }
        }
        return newProduct
    }

    suspend fun getFavoritesFood(): List<Food> {
        return foodRoomDao.getFavoritesFood(userEmail)
    }

    suspend fun saveFavoritesFood(food: Food): Answer {
        foodRoomDao.saveFavoritesFood(food)
        return Answer(1, "true")
    }

    suspend fun deleteFavoritesFood(food: Food): Answer {
        food.yemek_adi?.let { foodRoomDao.deleteFavoritesFood(it) }
        return Answer(1, "false")
    }

    suspend fun getOrder(): List<Order> {
        return foodRoomDao.getOrder(userEmail)
    }

    suspend fun saveOrder(order: Order): Answer {
        Log.e("liste", order.toString())
        foodRoomDao.saveOrder(order)
        return Answer(1, "true")
    }

    //Firebse
    fun login(email: String, password: String): MutableLiveData<Answer> {
        answerLogin.value = Answer(null, null)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                answerLogin.value = Answer(1,"")
            }
        }.addOnFailureListener {
            answerLogin.value = Answer(0,it.message)
        }
        return answerLogin
    }

    fun register(email: String, password: String): MutableLiveData<Answer> {
        answerRegister.value = Answer(null, null)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val uid = firebaseUser.uid
                    val user = User(uid, email, 0, "none")
                    collectionReference.document(uid).set(user).addOnSuccessListener {
                        answerRegister.value = Answer(1, "")
                    }
                }
            }.addOnFailureListener {
                answerRegister.value = Answer(0, it.message)
            }
        return answerRegister
    }

    fun getLiveUser(): MutableLiveData<User> {
        userId = firebaseAuth.currentUser?.uid
        userId?.let {
            collectionReference.document(it).addSnapshotListener { value, _ ->
                if (value != null) {
                    val hasmapp: HashMap<String, Any?> = value.data as HashMap<String, Any?>
                    val liveUser = User(
                        hasmapp["userId"].toString(),
                        hasmapp["userEmail"].toString(),
                        hasmapp["userBalance"].toString().toInt(),
                        hasmapp["ppUrl"].toString()
                    )
                    userLiveData.value = liveUser
                }
            }
        }
        return userLiveData
    }

    fun updateBalance(lastBalance: Int) {
        val wallet = HashMap<String, Any>()
        wallet["userBalance"] = lastBalance
        userId?.let {
            collectionReference.document(it).update(wallet)
        }
    }

    fun updateImage(imageUrl: String) {
        val image = HashMap<String, Any>()
        image["ppUrl"] = imageUrl
        userId?.let {
            collectionReference.document(it).update(image)
        }
    }

    //Preferences
    fun loadModePreferences(languageMode: Boolean, displayMode: Boolean) =
        appSharedPref.loadModePreferences(languageMode, displayMode)

    fun getModePreferences() = appSharedPref.getModePrefences()
}




