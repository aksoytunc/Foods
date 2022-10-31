package com.tuncaksoy.inviobitirmeprojesi.listener

interface BasketCheckClickListener {
    fun acceptClick( cardNumber: String?, adress: String?)
    fun cancelClick()
}