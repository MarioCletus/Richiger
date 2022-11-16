package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository (private  val userDao: UserDao) {

    @WorkerThread
    suspend fun insertUserData(user: User){
        userDao.insertUser(user)
    }

    val allUserList: Flow<List<User>> = userDao.getAllUserList()
}