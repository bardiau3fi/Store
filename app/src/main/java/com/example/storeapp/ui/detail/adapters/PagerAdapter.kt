package com.example.storeapp.ui.detail.adapters

import com.example.storeapp.ui.detail_chart.DetailChartFragment
import com.example.storeapp.ui.detail_comments.DetailCommentsFragment
import com.example.storeapp.ui.detail_features.DetailFeaturesFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailCommentsFragment()
            1 -> DetailFeaturesFragment()
            else -> DetailChartFragment()
        }
    }
}