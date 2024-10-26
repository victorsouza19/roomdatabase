package com.example.roomdatabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomdatabase.application.RoomApplication
import com.example.roomdatabase.database.AppDatabase
import com.example.roomdatabase.database.daos.UserDao
import com.example.roomdatabase.database.models.User
import com.example.roomdatabase.databinding.ActivityUserRegisterBinding
import com.example.roomdatabase.ui.viewmodels.UserViewModel
import com.example.roomdatabase.ui.viewmodels.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegisterBinding

    private val userViewModel : UserViewModel by viewModels {
        UserViewModelFactory((application as RoomApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
    }

    override fun onStart() {
        super.onStart()

        this.binding.btnSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val result = saveUser(
                    binding.inputFirstName.text.toString(),
                    binding.inputLastName.text.toString()
                )

                withContext(Dispatchers.Main){

                    Toast.makeText(this@UserRegisterActivity,
                        if(result) "User saved!" else "Error trying to save user",
                        Toast.LENGTH_SHORT
                    ).show()

                    if(result)
                        finish()
                }
            }
        }

        this.binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun saveUser(firstName: String, lastName: String): Boolean {
        if(firstName.isBlank() || firstName.isEmpty()){
            return false
        }

        if(lastName.isBlank() || firstName.isEmpty()){
            return false
        }

        userViewModel.insert(User(firstName, lastName))
        return true
    }
}