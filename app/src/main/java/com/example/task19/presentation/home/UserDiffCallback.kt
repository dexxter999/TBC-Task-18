package com.example.task19.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.example.task19.domain.model.User

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
}