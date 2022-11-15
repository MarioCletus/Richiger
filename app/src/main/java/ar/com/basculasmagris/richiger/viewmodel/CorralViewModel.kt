package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.CorralRepository
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.CorralRemote
import com.basculasmagris.richiger.model.network.CorralApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class CorralViewModel (private val repository: CorralRepository) : ViewModel() {

    // Remote
    private val corralApiService = CorralApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadCorral = MutableLiveData<Boolean>()
    val corralsResponse = MutableLiveData<List<CorralRemote>?>()
    val corralsLoadingError = MutableLiveData<Boolean>()

    // Local
    fun insert(corral: Corral) = viewModelScope.launch {
        repository.insertCorralData(corral)
    }

    suspend fun insertSync(corral: Corral) = repository.insertCorralData(corral)

    val allCorralList: LiveData<MutableList<Corral>> = repository.allCorralList.asLiveData()
    val activeCorralList: LiveData<List<Corral>> = repository.activeCorralList.asLiveData()

    fun getCorralById(id: Int) : LiveData<Corral> = repository.getCorralById(id).asLiveData()

    fun getFilteredCorralList(value: String): LiveData<List<Corral>> =
        repository.getFilteredCorralList(value).asLiveData()

    fun update(corral: Corral) = viewModelScope.launch {
        repository.updateCorralData(corral)
    }

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun setUpdatedEstablishmentRemoteId(id: Long, establishmentRemoteId: Long) = viewModelScope.launch {
        repository.setUpdatedEstablishmentRemoteId(id, establishmentRemoteId)
    }

    fun delete(corral: Corral) = viewModelScope.launch {
        repository.deleteCorralData(corral)
    }
}

class CorralViewModelFactory(private val repository: CorralRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CorralViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CorralViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}