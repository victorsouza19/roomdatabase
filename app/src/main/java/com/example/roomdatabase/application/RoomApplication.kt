package com.example.roomdatabase.application

import android.app.Application
import com.example.roomdatabase.database.AppDatabase
import com.example.roomdatabase.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RoomApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getInstance(applicationScope, this) }
    val repository by lazy { UserRepository(database.userDao()) }

}