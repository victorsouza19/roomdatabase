package com.example.roomdatabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabase.application.RoomApplication
import com.example.roomdatabase.database.models.User
import com.example.roomdatabase.databinding.ActivityUserInfoBinding
import com.example.roomdatabase.ui.viewmodels.UserViewModel
import com.example.roomdatabase.ui.viewmodels.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var user: User

    private val userViewModel : UserViewModel by viewModels {
        UserViewModelFactory((application as RoomApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
    }

    override fun onStart() {
        super.onStart()

        val userId = intent.getIntExtra("user_id", -1)
        if(userId != -1){
            userViewModel.getUserById(userId).observe(this) {
                it?.let {
                    user = it
                    configureUI()
                } ?: run {
                    notifyUserDeletedAndFinish()
                }
            }
        }
    }

    private fun configureUI(){
        binding.inputFirstName.setText(user.firstName)
        binding.inputLastName.setText(user.lastName)

        binding.btnEdit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val result = editUser(
                    binding.inputFirstName.text.toString(),
                    binding.inputLastName.text.toString()
                )

                withContext(Dispatchers.Main){
                    notifyUserUpdatedAndFinish(result)
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                deleteUser()
            }
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun deleteUser(){
        userViewModel.delete(user)
    }

    private fun editUser(firstName: String, lastName: String): Boolean {
        if(firstName.isBlank() || firstName.isEmpty()){
            return false
        }

        if(lastName.isBlank() || firstName.isEmpty()){
            return false
        }

        if(firstName != user.firstName || lastName != user.lastName){
            userViewModel.update(User(uid = user.uid, firstName = firstName, lastName = lastName))
        }

        return true
    }

    private fun notifyUserDeletedAndFinish(){
        Toast.makeText(this@UserInfoActivity,
            "User deleted!",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun notifyUserUpdatedAndFinish(success: Boolean){
        Toast.makeText(this@UserInfoActivity,
            if(success) "User updated!" else "Error trying to update user",
            Toast.LENGTH_SHORT
        ).show()

        if(success)
            finish()
    }

}