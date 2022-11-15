package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.MixerRepository
import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.model.entities.MixerRemote
import com.basculasmagris.richiger.model.network.MixerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class MixerViewModel (private val repository: MixerRepository) : ViewModel() {

    // Remote
    private val mixerApiService = MixerApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadMixer = MutableLiveData<Boolean>()
    val mixersResponse = MutableLiveData<List<MixerRemote>?>()
    val mixersLoadingError = MutableLiveData<Boolean>()

    fun getMixersFromAPI() {
        loadMixer.value = true
        compositeDisposable.add(
            mixerApiService.getMixers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<MixerRemote>>() {
                    override fun onSuccess(value: List<MixerRemote>?) {
                        loadMixer.value = true
                        mixersResponse.value = value
                        mixersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadMixer.value = false
                        mixersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }
                )
        )
    }

    // Local
    fun insert(mixer: Mixer) = viewModelScope.launch {
        repository.insertMixerData(mixer)
    }

    suspend fun insertSync(mixer: Mixer) = repository.insertMixerData(mixer)

    val allMixerList: LiveData<MutableList<Mixer>> = repository.allMixerList.asLiveData()
    val activeMixerList: LiveData<List<Mixer>> = repository.activeMixerList.asLiveData()

    fun getMixerById(id: Long) : LiveData<Mixer> = repository.getMixerById(id).asLiveData()

    fun getFilteredMixerList(value: String): LiveData<List<Mixer>> =
        repository.getFilteredMixerList(value).asLiveData()

    fun update(mixer: Mixer) = viewModelScope.launch {
        repository.updateMixerData(mixer)
    }

    suspend fun updateSync(mixer: Mixer) = repository.updateMixerData(mixer)

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(mixer: Mixer) = viewModelScope.launch {
        repository.deleteMixerData(mixer)
    }
}

class MixerViewModelFactory(private val repository: MixerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MixerViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MixerViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}