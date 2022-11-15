package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.RoundRepository
import com.basculasmagris.richiger.model.entities.*
import com.basculasmagris.richiger.model.network.RoundApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class RoundViewModel(private val repository: RoundRepository) : ViewModel() {

    // Remote
    private val roundApiService = RoundApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadRound = MutableLiveData<Boolean>()
    val roundsResponse = MutableLiveData<List<RoundRemote>?>()
    val roundsLoadingError = MutableLiveData<Boolean>()

    // Local
    fun insert(round: Round) = viewModelScope.launch {
        repository.insertRoundData(round)
    }

    suspend fun insertSync(round: Round) = repository.insertRoundData(round)

    fun insertRoundCorral(roundCorral: RoundCorral) = viewModelScope.launch {
        repository.insertRoundCorralData(roundCorral)
    }

    val allRoundList: LiveData<MutableList<Round>> = repository.allRoundList.asLiveData()
    val activeRoundList: LiveData<MutableList<Round>> = repository.activeRoundList.asLiveData()
    val allRoundCorralList: LiveData<MutableList<RoundCorral>> = repository.getAllRoundCorralList.asLiveData()

    fun getCorralsBy(idRound: Long) : LiveData<MutableList<RoundCorralDetail>> = repository.getCorralsBy(idRound).asLiveData()

    fun getRoundById(id: Long) : LiveData<Round> = repository.getRoundById(id).asLiveData()

    fun getFilteredRoundList(value: String): LiveData<List<Round>> =
        repository.getFilteredRoundList(value).asLiveData()

    fun update(round: Round) = viewModelScope.launch {
        repository.updateRoundData(round)
    }

    suspend fun updateSync(round: Round) = repository.updateRoundData(round)

    fun updateCorralBy(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double) = viewModelScope.launch {
        repository.updateRoundCorralData(roundId, corralId, order, weight, percentage)
    }

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(round: Round) = viewModelScope.launch {
        repository.deleteRoundData(round)
    }
    fun deleteCorralBy(roundId: Long, corralId: Long) = viewModelScope.launch {
        repository.deleteRoundCorralData(roundId, corralId)
    }

    suspend fun deleteCorralByRound(roundId: Long) = repository.deleteCorralByRoundData(roundId)
}

class RoundViewModelFactory(private val repository: RoundRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RoundViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}