package com.example.drinkup.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkup.viewmodel.DrinkViewModel
import com.example.drinkup.R
import com.example.drinkup.databinding.ActivityMainBinding
import com.example.drinkup.utils.PreferencesManager
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DrinkViewModel by viewModels()
    private lateinit var prefsManager: PreferencesManager

    private val REQUEST_NOTIFICATION_PERMISSION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        // 📌 Ask notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
        }

        // 📌 Load logged user
        val userId = prefsManager.currentUserId
        if (userId != -1L) {
            viewModel.setCurrentUser(userId)
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = MainPagerAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_dashboard)
                1 -> getString(R.string.tab_goals)
                2 -> getString(R.string.tab_tips)
                3 -> getString(R.string.tab_profile)
                else -> ""
            }

            tab.setIcon(
                when (position) {
                    0 -> R.drawable.ic_dashboard
                    1 -> R.drawable.ic_goal
                    2 -> R.drawable.ic_tips
                    3 -> R.drawable.ic_profile
                    else -> R.drawable.ic_dashboard
                }
            )
        }.attach()
    }

    // 📌 Handle permission result (optional)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            // Just info, no crash
        }
    }
}
