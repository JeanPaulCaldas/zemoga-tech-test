package com.zemoga.posts.ui.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.zemoga.posts.R
import com.zemoga.posts.databinding.FragmentPagerBinding


class PagerFragment : Fragment() {

    private var _binding: FragmentPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerBinding.inflate(inflater, container, false).setup()
        return binding.root
    }

    private fun FragmentPagerBinding.setup() = this.apply {

        pager.adapter = PostPagerAdapter(this@PagerFragment)

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            val tabTitle =
                if (position == 0) R.string.posts_pager_all_tab else R.string.posts_pager_fav_tab
            tab.setText(tabTitle)
        }.attach()
    }
}