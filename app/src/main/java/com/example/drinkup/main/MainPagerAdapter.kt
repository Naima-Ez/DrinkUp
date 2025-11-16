package com.example.drinkup.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.drinkup.fragments.DashboardFragment
import com.example.drinkup.fragments.ProfileFragment
import com.example.drinkup.fragments.TipsFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            2 -> TipsFragment()
            3 -> ProfileFragment()
            else -> DashboardFragment()
        }
    }
}
