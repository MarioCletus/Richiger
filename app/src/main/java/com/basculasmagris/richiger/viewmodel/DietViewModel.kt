package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.DietRepository
import com.basculasmagris.richiger.model.entities.*
import com.basculasmagris.richiger.model.network.DietApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class DietViewModel(private val repository: DietRepository) : ViewModel() {

    // Remote
    private val dietApiService = DietApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadDiet = MutableLiveData<Boolean>()
    val dietsResponse = MutableLiveData<List<DietRemote>?>()
    val dietsLoadingError = MutableLiveData<Boolean>()

    // Local
    fun insert(diet: Diet)  = viewModelScope.launch {
        repository.insertDietData(diet)
    }

    suspend fun insertSync(diet: Diet)  = repository.insertDietData(diet)


    fun insertDietProduct(dietProduct: DietProduct) = viewModelScope.launch {
        repository.insertDietProductData(dietProduct)
    }

    val allDietList: LiveData<MutableList<Diet>> = repository.allDietList.asLiveData()
    val activeDietList: LiveData<MutableList<Diet>> = repository.activeDietList.asLiveData()
    val allDietProductList: LiveData<MutableList<DietProduct>> = repository.getAllDietProductList.asLiveData()

    fun getProductsBy(idDiet: Long) : LiveData<MutableList<DietProductDetail>> = repository.getProductsBy(idDiet).asLiveData()
    fun getSyncProductsBy(idDiet: Long) : List<DietProductDetail> = repository.getSyncProductsBy(idDiet)

    fun getDietById(id: Long) : LiveData<Diet> = repository.getDietById(id).asLiveData()

    fun getFilteredDietList(value: String): LiveData<List<Diet>> =
        repository.getFilteredDietList(value).asLiveData()

    fun update(diet: Diet) = viewModelScope.launch {
        repository.updateDietData(diet)
    }

    suspend  fun updateSync(diet: Diet) = repository.updateDietData(diet)

    fun updateProductBy(dietId: Long, productId: Long, order: Int, weight: Double, percentage: Double) = viewModelScope.launch {
        repository.updateDietProductData(dietId, productId, order, weight, percentage)
    }

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(diet: Diet) = viewModelScope.launch {
        repository.deleteDietData(diet)
    }
    fun deleteProductBy(dietId: Long, productId: Long) = viewModelScope.launch {
        repository.deleteDietProductData(dietId, productId)
    }

    suspend  fun deleteProductByDiet(dietId: Long) = repository.deleteProductsByDietData(dietId)
}

class DietViewModelFactory(private val repository: DietRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DietViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DietViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}