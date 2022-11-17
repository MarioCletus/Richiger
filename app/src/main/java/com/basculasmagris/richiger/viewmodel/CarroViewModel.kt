package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.CarroRepository
import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.model.entities.CarroRemote
import com.basculasmagris.richiger.model.network.CarroApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*

class CarroViewModel (private val repository: CarroRepository) : ViewModel() {

    // Remote
    private val carroApiService = CarroApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadCarro = MutableLiveData<Boolean>()
    val carrosResponse = MutableLiveData<List<CarroRemote>?>()
    val carrosLoadingError = MutableLiveData<Boolean>()

    fun getCarrosFromAPI() {
        loadCarro.value = true
        compositeDisposable.add(
            carroApiService.getCarros()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CarroRemote>>() {
                    override fun onSuccess(value: List<CarroRemote>?) {
                        loadCarro.value = true
                        carrosResponse.value = value
                        carrosLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadCarro.value = false
                        carrosLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }
                )
        )
    }

    // Local
    fun insert(carro: Carro) = viewModelScope.launch {
        repository.insertCarroData(carro)
    }

    suspend fun insertSync(carro: Carro) = repository.insertCarroData(carro)

    val allCarroList: LiveData<MutableList<Carro>> = repository.allCarroList.asLiveData()
    val activeCarroList: LiveData<List<Carro>> = repository.activeCarroList.asLiveData()

    fun getCarroById(id: Long) : LiveData<Carro> = repository.getCarroById(id).asLiveData()

    fun getFilteredCarroList(value: String): LiveData<List<Carro>> =
        repository.getFilteredCarroList(value).asLiveData()

    fun update(carro: Carro) = viewModelScope.launch {
        repository.updateCarroData(carro)
    }

    suspend fun updateSync(carro: Carro) = repository.updateCarroData(carro)

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(carro: Carro) = viewModelScope.launch {
        repository.deleteCarroData(carro)
    }
}

class CarroViewModelFactory(private val repository: CarroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarroViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CarroViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}