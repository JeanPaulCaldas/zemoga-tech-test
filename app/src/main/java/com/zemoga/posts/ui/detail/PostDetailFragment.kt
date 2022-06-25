package com.zemoga.posts.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zemoga.posts.databinding.FragmentPostDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val viewModel: PostDetailViewModel by viewModels()

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1
        )
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(uiState: PostDetailViewModel.PostDetailUiState) {
        adapter.addAll(uiState.comments)

        uiState.post?.let { binding.txtDescription.text = it.description }

        uiState.author?.let {
            binding.txtName.text = it.name
            binding.txtEmail.text = it.email
            binding.txtPhone.text = it.phone
            binding.txtWebsite.text = it.website
        }
    }

    companion object {
        const val POST_ID = "PostDetailFragment.PostId"

        @JvmStatic
        fun newInstance(postId: Int) =
            PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(POST_ID, postId)
                }
            }
    }
}