package com.example.drinkup.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkup.DrinkViewModel
import com.example.drinkup.R
import com.example.drinkup.databinding.ActivityMainBinding
import com.example.drinkup.utils.PreferencesManager
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DrinkViewModel by viewModels()
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        // Charger l'utilisateur actuel
        // Charger l'utilisateur actuel
        val userId = prefsManager.currentUserId
        if (userId != -1L) {
            viewModel.loadUser(userId) // ila f ViewModel dyalek smiyt method loadUser
        }


        fun setupViewPager() {
        val adapter = MainPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Désactiver le swipe si nécessaire
        binding.viewPager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_dashboard)
                1 -> getString(R.string.tab_goals)
                2 -> getString(R.string.tab_tips)
                3 -> getString(R.string.tab_profile)
                else -> ""
            }

            tab.setIcon(when (position) {
                0 -> R.drawable.ic_dashboard
                1 -> R.drawable.ic_goal
                2 -> R.drawable.ic_tips
                3 -> R.drawable.ic_profile
                else -> R.drawable.ic_dashboard
            })
        }.attach()
    }
}
     }

