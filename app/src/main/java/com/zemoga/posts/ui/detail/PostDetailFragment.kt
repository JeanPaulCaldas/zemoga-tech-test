package com.zemoga.posts.ui.detail

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zemoga.posts.R
import com.zemoga.posts.databinding.FragmentPostDetailBinding
import com.zemoga.posts.ui.detail.PostDetailViewModel.PostDetailEvent.DeletePost
import com.zemoga.posts.ui.detail.PostDetailViewModel.PostDetailEvent.ToggleFavorite
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    //region Properties
    private val viewModel: PostDetailViewModel by viewModels()

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
    }
    //endregion

    //region Fragment Overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false).apply {
            listComments.adapter = adapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach(::render).collect()
            }
        }

        viewModel.actionState.observe(viewLifecycleOwner) {
            when (it) {
                is PostDetailViewModel.PostDetailActionState.Error -> showError()
                PostDetailViewModel.PostDetailActionState.NavigateOut -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_favorite -> {
                viewModel.setEvent(ToggleFavorite)
                true
            }
            R.id.action_delete_post -> {
                viewModel.setEvent(DeletePost)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_toggle_favorite)
        val icon =
            if (viewModel.uiState.value.isFavoritePost) R.drawable.ic_round_star else R.drawable.ic_round_star_border
        item.icon = resources.getDrawable(icon, requireContext().theme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region Private Methods
    private fun render(uiState: PostDetailViewModel.PostDetailUiState) {
        adapter.clear()
        adapter.addAll(uiState.comments)

        uiState.post?.let {
            binding.txtDescription.text = it.description
            requireActivity().invalidateOptionsMenu()
        }

        uiState.author?.let {
            binding.txtName.text = getString(R.string.post_detail_author_name, it.name)
            binding.txtEmail.text = getString(R.string.post_detail_author_email, it.email)
            binding.txtPhone.text = getString(R.string.post_detail_author_phone, it.phone)
            binding.txtWebsite.text = getString(R.string.post_detail_author_website, it.website)
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), getString(R.string.error_message), Toast.LENGTH_SHORT)
            .show()
    }
    //endregion
}