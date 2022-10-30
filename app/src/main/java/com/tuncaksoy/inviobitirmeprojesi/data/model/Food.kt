package com.tuncaksoy.inviobitirmeprojesi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "foods_fav")
data class Food(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "food_id")
    var food_id: Int,
    @ColumnInfo(name = "sepet_yemek_id")
    var sepet_yemek_id: Int?,
    @ColumnInfo(name = "yemek_id")
    var yemek_id: String?,
    @ColumnInfo(name = "yemek_adi")
    var yemek_adi: String?,
    @ColumnInfo(name = "yemek_resim_adi")
    var yemek_resim_adi: String?,
    @ColumnInfo(name = "yemek_fiyat")
    var yemek_fiyat: Int?,
    @ColumnInfo(name = "yemek_siparis_adet")
    var yemek_siparis_adet: String?,
    @ColumnInfo(name = "kullanici_adi")
    var kullanici_adi: String?,
    @ColumnInfo(name = "yemek_sepet")
    var yemek_sepet: Boolean?,
    @ColumnInfo(name = "yemek_favori")
    var yemek_favori: Boolean?
) : java.io.Serializable {
}