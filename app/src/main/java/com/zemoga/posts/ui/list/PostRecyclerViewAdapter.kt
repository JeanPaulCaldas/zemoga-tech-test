package com.zemoga.posts.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zemoga.core.domain.Post
import com.zemoga.posts.databinding.ItemPostBinding
import com.zemoga.posts.ui.util.visible

class PostRecyclerViewAdapter(private val onClick: (postId: Int) -> Unit) :
    ListAdapter<Post, PostRecyclerViewAdapter.ViewHolder>(PostDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) = binding.apply {
            favoriteIcon.visible = post.favorite
            title.text = post.title
            this.root.setOnClickListener { onClick(post.id) }
        }
    }

}

internal object PostDiff : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem
}