package com.tuncaksoy.inviobitirmeprojesi.listener

import android.view.View

interface RegisterClickListener {
    fun btnRegisterClick(view: View, email: String?, password: String?)
}