package com.zemoga.posts.ui.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zemoga.posts.databinding.FragmentPostListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    private val viewModel: PostListViewModel by viewModels()

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!
    private var favoriteView = false
    private lateinit var recyclerAdapter: PostRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { favoriteView = it.getBoolean(ARG_FAV_VIEW) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false).apply {
            recyclerAdapter = PostRecyclerViewAdapter()
            recycler.adapter = recyclerAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState
                        .onEach {
                            recyclerAdapter.submitList(it.posts)
                        }.collect()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_FAV_VIEW = "favorite_view"

        @JvmStatic
        fun newInstance(favoriteView: Boolean) =
            PostsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FAV_VIEW, favoriteView)
                }
            }
    }
}