package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.ActivityLogoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logout)
        binding.splashScreen.setOnClickListener {}
        val timer = object : CountDownTimer(2500, 2500) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                binding.splashScreen.visibility = View.GONE
                window.statusBarColor = ContextCompat.getColor(this@LogoutActivity, R.color.white)
            }
        }
        timer.start()
    }

    fun login() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}