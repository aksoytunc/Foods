package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.ActivityMainBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewMoldel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMoldel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        controlDisplayData()
        if (viewMoldel.firebaseAuth.currentUser == null) {
            logOut()
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)
        viewMoldel.getModePreferences()
        val timer = object : CountDownTimer(2500, 2500) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                binding.splashScreen.visibility = View.GONE
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.white)
            }
        }
        timer.start()
    }

    fun controlDisplayData() {
        if (viewMoldel.getModePreferences().displayMode) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    fun logOut() {
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
        viewMoldel.firebaseAuth.signOut()
        finish()
    }
}