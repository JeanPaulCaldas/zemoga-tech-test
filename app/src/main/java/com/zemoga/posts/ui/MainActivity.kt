package com.zemoga.posts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.zemoga.posts.R
import com.zemoga.posts.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).setup()
        setContentView(binding.root)
    }

    private fun ActivityMainBinding.setup() = this.apply {

        pager.adapter = PostPagerAdapter(this@MainActivity)

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            val tabTitle =
                if (position == 0) R.string.posts_pager_all_tab else R.string.posts_pager_fav_tab
            tab.setText(tabTitle)
        }.attach()
    }
}

