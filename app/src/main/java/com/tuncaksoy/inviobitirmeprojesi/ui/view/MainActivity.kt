package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Display.Mode
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.ActivityMainBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewMoldel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    var auth = FirebaseAuth.getInstance()
    val rememberUser = auth.currentUser
    var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMoldel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)
        viewMoldel.getModePrefences()
        observeLiveData()

        val timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time += 1000
            }
            override fun onFinish() {
                if (time == 2000L) {
                    binding.splashScreen.visibility = View.GONE
                    window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.white)
                    if (rememberUser == null) {
                        Log.e("saybakem", "1234")
                        logOut()
                    }
                }
            }
        }
        timer.start()

    }

    fun observeLiveData() {
        Log.d("displaymode", "geldikyoktunuz")
        viewMoldel.displayData.observe(this) {
            Log.d("displaymode", "deneme" + it.toString())
            if (it.displayMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun logOut() {
        Log.d("saybakem", "1234")
        auth.signOut()
        viewMoldel.deleteUserIdPref()
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
        finish()
    }
}