package com.tuncaksoy.inviobitirmeprojesi.listener

import android.view.View

interface BasketCheckClickListener {
    fun acceptClick( cardNumber: String?, adress: String?)
    fun cancelClick()
}