package com.example.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdatabase.database.daos.UserDao
import com.example.roomdatabase.database.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME: String = "room-database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(scope: CoroutineScope, context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(scope, context).also { INSTANCE = it }
            }

        private fun buildDatabase(scope: CoroutineScope, context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).addCallback(UserDataBaseCallback(scope))
                .build()
    }

    private class UserDataBaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.userDao())
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            userDao.deleteAll()

            val firstName = setOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Julia")
            val lastName = setOf("Silva", "Souza", "Pereira", "Costa", "Santos", "Oliveira", "Ribeiro", "Almeida", "Nascimento", "Fernandes")

            for (index in 1..Random.nextInt(5, 20)) {
                userDao.insert(User(firstName.random(),  lastName.random()))
            }

        }

    }

}