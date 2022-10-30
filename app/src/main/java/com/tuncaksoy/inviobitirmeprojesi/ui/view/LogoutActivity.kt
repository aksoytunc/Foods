package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.LogoutActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutActivity : AppCompatActivity() {
    private lateinit var viewModel: LogoutActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        viewModel = ViewModelProvider(this).get(LogoutActivityViewModel::class.java)
    }

    fun login() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}