package com.tuncaksoy.inviobitirmeprojesi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "order_all")
data class Order(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "order_id")
    var orderId: Int,
    @ColumnInfo(name = "order_no")
    var orderNo: Int?,
    @ColumnInfo(name = "order_status")
    var orderStatus: Int?,
    @ColumnInfo(name = "address")
    var address: String?,
    @ColumnInfo(name = "card_wallet")
    var cardWallet: Boolean?,
    @ColumnInfo(name = "price")
    var price: Int?,
    @ColumnInfo(name = "product_number")
    var productNumber: Int?,
    @ColumnInfo(name = "user_email")
    var userEmail: String?,
    @ColumnInfo(name = "date_time")
    var dateTime: String?
) : java.io.Serializable {
}
