package com.myapp.githubuserupdate

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailTabAdapter(activity: AppCompatActivity, private val user: User): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FragmentFollowers(user)
            1 -> fragment = FragmentFollowing(user)
        }
        return fragment as Fragment
    }
}