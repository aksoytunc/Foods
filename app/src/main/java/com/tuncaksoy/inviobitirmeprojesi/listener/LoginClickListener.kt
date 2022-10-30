package com.tuncaksoy.inviobitirmeprojesi.listener

import android.view.View

interface LoginClickListener {
    fun btnLoginClick(view: View, email: String?, password: String?)
    fun txtRegisterClick(view: View)
}