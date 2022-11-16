package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.UserRepository
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository) : ViewModel() {

    fun insert(user: User) = viewModelScope.launch {
        repository.insertUserData(user)
    }

    val allUserList: LiveData<List<User>> = repository.allUserList.asLiveData()
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
           return UserViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}