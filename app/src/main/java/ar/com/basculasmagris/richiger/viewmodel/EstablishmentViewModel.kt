package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.EstablishmentRepository
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.model.entities.EstablishmentRemote
import com.basculasmagris.richiger.model.network.EstablishmentApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class EstablishmentViewModel(private val repository: EstablishmentRepository) : ViewModel() {

    // Remote
    private val establishmentApiService = EstablishmentApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadEstablishment = MutableLiveData<Boolean>()
    val establishmentsResponse = MutableLiveData<List<EstablishmentRemote>?>()
    val establishmentsLoadingError = MutableLiveData<Boolean>()

    // Local
    fun insert(establishment: Establishment) = viewModelScope.launch {
        repository.insertEstablishmentData(establishment)
    }

    suspend fun insertSync(establishment: Establishment) = repository.insertEstablishmentData(establishment)

    val allEstablishmentList: LiveData<MutableList<Establishment>> = repository.allEstablishmentList.asLiveData()
    val activeEstablishmentList: LiveData<List<Establishment>> = repository.activeEstablishmentList.asLiveData()

    fun getEstablishmentById(id: Long) : LiveData<Establishment> = repository.getEstablishmentById(id).asLiveData()

    fun getFilteredEstablishmentList(value: String): LiveData<List<Establishment>> =
        repository.getFilteredEstablishmentList(value).asLiveData()

    fun update(establishment: Establishment) = viewModelScope.launch {
        repository.updateEstablishmentData(establishment)
    }

    suspend fun updateSync(establishment: Establishment) = repository.updateEstablishmentData(establishment)

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(establishment: Establishment) = viewModelScope.launch {
        repository.deleteEstablishmentData(establishment)
    }
}

class EstablishmentViewModelFactory(private val repository: EstablishmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstablishmentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return EstablishmentViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}

class SyncMediatorLiveData<F, S, T>(
    EstablishmentLiveData: LiveData<F>,
    CorralLiveData: LiveData<S>,
    ProductLiveData: LiveData<T>
) : MediatorLiveData<Triple<F?, S?, T?>>() {
    init {
        addSource(EstablishmentLiveData) { EstablishmentLiveDataValue: F -> value = Triple(EstablishmentLiveDataValue, CorralLiveData.value, ProductLiveData.value) }
        addSource(CorralLiveData) { CorralLiveDataValue: S -> value = Triple(EstablishmentLiveData.value, CorralLiveDataValue, ProductLiveData.value) }
        addSource(ProductLiveData) { ProductLiveDataValue: T -> value = Triple(EstablishmentLiveData.value, CorralLiveData.value, ProductLiveDataValue) }
    }
}