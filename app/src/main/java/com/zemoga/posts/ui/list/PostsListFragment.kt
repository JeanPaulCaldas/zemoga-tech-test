package com.zemoga.posts.ui.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zemoga.posts.R
import com.zemoga.posts.databinding.FragmentPostListBinding
import com.zemoga.posts.ui.pager.PagerFragmentDirections
import com.zemoga.posts.ui.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsListFragment : Fragment() {

    //region Properties
    private val viewModel: PostListViewModel by viewModels()

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerAdapter: PostRecyclerViewAdapter
    //endregion

    //region Fragment Overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.setEvent(PostListContract.Event.GetAll)
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
                launch {
                    viewModel.uiState.onEach(::render).collect()
                }
                launch {
                    viewModel.effect.onEach(::handleEffect).collect()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.setEvent(PostListContract.Event.Refresh)
                true
            }
            R.id.action_delete_all -> {
                viewModel.setEvent(PostListContract.Event.DeleteAll)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region Private Methods
    private fun render(uiState: PostListContract.State) {
        when (val state = uiState.state) {
            PostListContract.PostListState.Idle -> {
                binding.progress.visible = false
            }
            PostListContract.PostListState.Loading -> {
                binding.progress.visible = true
            }
            is PostListContract.PostListState.Success -> {
                binding.progress.visible = false
                recyclerAdapter.submitList(state.posts)
            }
        }
    }

    private fun handleEffect(effect: PostListContract.Effect) {
        when (effect) {
            PostListContract.Effect.ShowError -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun goToPostDetail(postId: Int) {
        val action = PagerFragmentDirections.actionPagerFragmentToPostDetailFragment(postId)
        findNavController().navigate(action)
    }
    //endregion

    //region Static
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
    //endregion
}