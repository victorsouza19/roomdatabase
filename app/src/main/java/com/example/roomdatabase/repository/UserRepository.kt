package com.example.roomdatabase.repository

import androidx.annotation.WorkerThread
import com.example.roomdatabase.database.daos.UserDao
import com.example.roomdatabase.database.models.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAll()
    val itemsCount: Flow<Long> = userDao.getTotalItems()

    @WorkerThread
    suspend fun insert(user: User){
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun update(user: User){
        userDao.update(user)
    }

    @WorkerThread
    suspend fun delete(user: User){
        userDao.delete(user)
    }

    fun getUserById(userId: Int): Flow<User> {
        return userDao.getUserById(userId)
    }
}