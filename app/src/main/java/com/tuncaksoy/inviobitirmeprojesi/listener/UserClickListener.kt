package com.tuncaksoy.inviobitirmeprojesi.listener

interface UserClickListener {
    fun profileImageClick()
    fun loadProfileImageClick()
    fun layoutClick()
    fun loadBalanceClick(balance: String?, loadBalance: String?)
    fun logoutClick()
}