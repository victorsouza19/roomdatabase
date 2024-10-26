package com.example.roomdatabase.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.roomdatabase.database.models.User
import com.example.roomdatabase.databinding.ItemRecyclerViewUserBinding

class UserListAdapter(private val onItemClicked: (User) -> Unit) : ListAdapter<User, UserListAdapter.UserViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerViewUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onItemClicked)
    }

    class UserViewHolder(private val binding: ItemRecyclerViewUserBinding) : ViewHolder(binding.root) {

        fun bind(user: User, onItemClicked: (User) -> Unit){
            val name = user.firstName + " " + user.lastName
            binding.textView.text = name

            binding.layoutEditUser.setOnClickListener {
                onItemClicked(user)
            }
        }

    }

    class UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.firstName == newItem.firstName
                    && oldItem.lastName == newItem.lastName
        }
    }

}