package com.tuncaksoy.inviobitirmeprojesi.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tuncaksoy.inviobitirmeprojesi.data.Preferences.AppSharedPreferences
import com.tuncaksoy.inviobitirmeprojesi.data.datasource.FoodDataSource
import com.tuncaksoy.inviobitirmeprojesi.data.repository.FoodRepository
import com.tuncaksoy.inviobitirmeprojesi.retrofit.ApiUtils
import com.tuncaksoy.inviobitirmeprojesi.retrofit.FoodRetrofitDao
import com.tuncaksoy.inviobitirmeprojesi.retrofit.NetworkConnection
import com.tuncaksoy.inviobitirmeprojesi.room.FoodDatabase
import com.tuncaksoy.inviobitirmeprojesi.room.FoodRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFoodRepository(foodDataSource: FoodDataSource): FoodRepository {
        return FoodRepository(foodDataSource)
    }

    @Provides
    @Singleton
    fun provideFoodDataSource(
        foodRetrofitDao: FoodRetrofitDao,
        foodRoomDao: FoodRoomDao,
        firebaseAuth: FirebaseAuth,
        collectionReference: CollectionReference,
        appSharedPref: AppSharedPreferences
    ): FoodDataSource {
        return FoodDataSource(
            foodRetrofitDao,
            foodRoomDao,
            firebaseAuth,
            collectionReference,
            appSharedPref
        )
    }

    @Provides
    @Singleton
    fun provideFoodRetrofitDao(): FoodRetrofitDao {
        return ApiUtils.getFoodRetrofitDao()
    }

    @Provides
    @Singleton
    fun provideFoodRoomDao(@ApplicationContext context: Context): FoodRoomDao {
        val db = Room.databaseBuilder(context, FoodDatabase::class.java, "room_database.sqlite")
            .createFromAsset("room_database.sqlite").build()
        return db.getFoodRoomDao()
    }

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStore(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providefirebaseFirestore(): CollectionReference =
        FirebaseFirestore.getInstance().collection("User")

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): AppSharedPreferences =
        AppSharedPreferences(context)

    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkConnection =
        NetworkConnection((context))

}