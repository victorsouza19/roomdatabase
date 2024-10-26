package com.example.roomdatabase

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.roomdatabase.application.RoomApplication
import com.example.roomdatabase.databinding.ActivityMainBinding
import com.example.roomdatabase.ui.viewmodels.UserViewModel
import com.example.roomdatabase.ui.viewmodels.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val userViewModel : UserViewModel by viewModels {
        UserViewModelFactory((application as RoomApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.btnRegister.setOnClickListener {
            openRegisterUserActivity()
        }

        binding.btnShowUsers.setOnClickListener {
            openUserListActivity()
        }

        loadSavedItems()
    }

    private fun loadSavedItems() {
        binding.txtSavedUsers.text = "Loading data..."

        userViewModel.itemsCount.observe(this) { count ->
            count?.let { binding.txtSavedUsers.text = "Saved items: $it" }
        }
    }

    private fun openRegisterUserActivity() {
        startActivity(Intent(this@MainActivity, UserRegisterActivity::class.java))
    }

    private fun openUserListActivity() {
        startActivity(Intent(this@MainActivity, UserListActivity::class.java))
    }

}