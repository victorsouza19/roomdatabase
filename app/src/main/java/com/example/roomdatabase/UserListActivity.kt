package com.example.roomdatabase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.application.RoomApplication
import com.example.roomdatabase.database.models.User
import com.example.roomdatabase.databinding.ActivityUserListBinding
import com.example.roomdatabase.ui.adapters.UserListAdapter
import com.example.roomdatabase.ui.viewmodels.UserViewModel
import com.example.roomdatabase.ui.viewmodels.UserViewModelFactory

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListAdapter

    private val userViewModel : UserViewModel by viewModels {
        UserViewModelFactory((application as RoomApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.recyclerView = this.binding.rvUserList
        this.adapter = UserListAdapter {
            showUserInfoActivity(it)
        }

        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()

        userViewModel.allUsers.observe(this) { users ->
            users?.let {
                verifyListSizeAndUpdateAdapter(it)
            }
        }
    }

    private fun showUserInfoActivity(user: User){
        val intent = Intent(this, UserInfoActivity::class.java)
        intent.putExtra("user_id", user.uid)
        startActivity(intent)
    }

    private fun verifyListSizeAndUpdateAdapter(list: List<User>){
        adapter.submitList(list)
        binding.txtNoItemsAvailable.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }
}