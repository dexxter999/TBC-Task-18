package com.example.task19.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task19.databinding.UserRecyclerItemBinding
import com.example.task19.domain.model.User

class HomeRecyclerViewAdapter :
    ListAdapter<User, HomeRecyclerViewAdapter.UserViewHolder>(UserDiffCallback()) {

    private var onItemClick: ((User) -> Unit)? = null

    inner class UserViewHolder(private val binding: UserRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(ivAvatar)
            tvId.text = user.id.toString()
            tvEmail.text = user.email
            tvName.text = user.firstName
            tvLastName.text = user.lastName
            root.setOnClickListener { onItemClick?.invoke(user) }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun onItemClick(click: (User) -> Unit) {
        this.onItemClick = click
    }
}