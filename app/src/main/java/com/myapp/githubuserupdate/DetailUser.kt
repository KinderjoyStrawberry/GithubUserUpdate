package com.myapp.githubuserupdate

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myapp.githubuserupdate.databinding.ActivityDetailUserBinding

class DetailUser : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var u: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User's"

        u = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        val detailTabAdapter = DetailTabAdapter(this, u)
        val viewPage: ViewPager2 = binding.viewPager
        viewPage.adapter = detailTabAdapter
        val tab: TabLayout = binding.tabs
        TabLayoutMediator(tab, viewPage) { tab, position ->
            tab.text = resources.getString(TAB_NAME[position])
        }.attach()
        supportActionBar?.elevation = 0f
        getData()
    }

//    private fun getData() {
//        binding.loading.visibility = View.INVISIBLE
//        Glide.with(baseContext).load(u.avatar).circleCrop().into(binding.detailImgAva)
//        binding.detailName.text = u.name
//        binding.detailUsername.text = u.username
//        binding.detailFollowers.text = u.followers
//        binding.detailFollowing.text = u.following
//        binding.detailRepository.text = u.repository
//        binding.detailCompany.text = u.company
//        binding.detailLocation.text = u.location
//    }

    private fun getData(){
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.loading.visibility = View.INVISIBLE
            binding.apply {
                Glide.with(baseContext).load(u.avatar).circleCrop().into(binding.detailImgAva)
                detailName.text = u.name
                detailUsername.text = u.username
                detailFollowers.text = u.followers
                detailFollowing.text = u.following
                detailRepository.text = u.repository
                detailCompany.text = u.company
                detailLocation.text = u.location
            }


        } else {
            binding.apply {
                Glide.with(baseContext).load(u.avatar).circleCrop().into(binding.detailImgAva)
                detailName.text = u.name
                detailUsername.text = u.username
                detailFollowers.text = u.followers
                detailFollowing.text = u.following
                detailRepository.text = u.repository
                detailCompany.text = u.company
                detailLocation.text = u.location
            }
        }
    }

    companion object{
        const val EXTRA_DATA = "extra_data"

        @StringRes
        private val TAB_NAME = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}