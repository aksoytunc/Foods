package com.tuncaksoy.inviobitirmeprojesi.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.tuncaksoy.inviobitirmeprojesi.R
import com.tuncaksoy.inviobitirmeprojesi.databinding.ActivityMainBinding
import com.tuncaksoy.inviobitirmeprojesi.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationsBadges: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.splashScreen.setOnClickListener {}
        controlDisplayData()
        if (viewModel.firebaseAuth.currentUser == null) {
            logOut()
        }

        val itemView: BottomNavigationItemView? =
            binding.bottomNav.getChildAt(2) as? BottomNavigationItemView
        notificationsBadges = LayoutInflater.from(this).inflate(R.layout.badge_text, itemView, true)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)
        viewModel.getModePreferences()
        observeLiveData()
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

    private fun observeLiveData() {
        viewModel.basketFoodList.observe(this) {
            updateBadgeCount(it.size)
        }
    }

    private fun controlDisplayData() {
        if (viewModel.getModePreferences().displayMode) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        )
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun logOut() {
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
        viewModel.firebaseAuth.signOut()
        finish()
    }

    private fun updateBadgeCount(count: Int) {
        if (count == 0) {
            binding.bottomNav.removeView(notificationsBadges)
        } else {
            binding.bottomNav.removeView(notificationsBadges)
            val badgeText = notificationsBadges.findViewById<TextView>(R.id.notification_badge2)
            badgeText.text = count.toString()
            binding.bottomNav.addView(notificationsBadges)
        }
    }
}