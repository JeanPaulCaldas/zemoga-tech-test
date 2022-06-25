package com.zemoga.posts.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zemoga.posts.databinding.FragmentPostListBinding
import com.zemoga.posts.ui.detail.PostDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    private val viewModel: PostListViewModel by viewModels()

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerAdapter: PostRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false).apply {
            recyclerAdapter = PostRecyclerViewAdapter(::goToPostDetail)
            recycler.adapter = recyclerAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { render(it) }.collect()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(it: PostListViewModel.PostListUIState) {
        recyclerAdapter.submitList(it.posts)
    }

    private fun goToPostDetail(postId:Int){
        parentFragmentManager.commit {
            add(PostDetailFragment.newInstance(postId), "post_detail_fragment")
        }
    }

    companion object {
        const val FAVORITE = "PostsListFragment.Favorite"

        @JvmStatic
        fun newInstance(favoriteView: Boolean) =
            PostsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(FAVORITE, favoriteView)
                }
            }
    }
}