package com.basculasmagris.richiger.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUserList(): Flow<List<User>>
}