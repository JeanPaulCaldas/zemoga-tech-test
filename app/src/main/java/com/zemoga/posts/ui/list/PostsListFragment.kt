package com.zemoga.posts.ui.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zemoga.posts.R
import com.zemoga.posts.databinding.FragmentPostListBinding
import com.zemoga.posts.ui.detail.PostDetailFragment
import com.zemoga.posts.ui.list.PostListViewModel.PostListEvent.DeleteAll
import com.zemoga.posts.ui.list.PostListViewModel.PostListEvent.Refresh
import com.zemoga.posts.ui.pager.PagerFragmentDirections
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.setEvent(Refresh)
                true
            }
            R.id.action_delete_all -> {
                viewModel.setEvent(DeleteAll)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(it: PostListViewModel.PostListUIState) {
        recyclerAdapter.submitList(it.posts)
    }

    private fun goToPostDetail(postId: Int) {
        val action = PagerFragmentDirections.actionPagerFragmentToPostDetailFragment(postId)
        findNavController().navigate(action)
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