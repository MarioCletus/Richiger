package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.database.ProductRepository
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.model.entities.ProductRemote
import com.basculasmagris.richiger.model.network.ProductApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*

class ProductViewModel (private val repository: ProductRepository) : ViewModel() {

    // Remote
    private val productApiService = ProductApiService()
    private val compositeDisposable = CompositeDisposable()
    val loadProduct = MutableLiveData<Boolean>()
    val productsResponse = MutableLiveData<List<ProductRemote>?>()
    val productsLoadingError = MutableLiveData<Boolean>()

    fun getProductsFromAPI() {
        loadProduct.value = true
        compositeDisposable.add(
            productApiService.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ProductRemote>>() {
                    override fun onSuccess(value: List<ProductRemote>?) {
                        loadProduct.value = true
                        productsResponse.value = value
                        productsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadProduct.value = false
                        productsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }
            )
        )
    }

    // Local
    fun insert(product: Product) = viewModelScope.launch {
        repository.insertProductData(product)
    }

    suspend fun insertSync(product: Product) = repository.insertProductData(product)

    val allProductList: LiveData<MutableList<Product>> = repository.allProductList.asLiveData()
    val activeProductList: LiveData<List<Product>> = repository.allActiveList.asLiveData()

    fun getProductById(id: Long) : LiveData<Product> = repository.getProductById(id).asLiveData()

    fun getFilteredProductList(value: String): LiveData<List<Product>> =
        repository.getFilteredProductList(value).asLiveData()

    fun update(product: Product) = viewModelScope.launch {
        repository.updateProductData(product)
    }

    suspend fun updateSync(product: Product) = repository.updateProductData(product)

    fun setUpdatedDate(id: Long, date: String) = viewModelScope.launch {
        repository.setUpdatedDate(id, date)
    }

    fun setUpdatedRemoteId(id: Long, remoteId: Long) = viewModelScope.launch {
        repository.setUpdatedRemoteId(id, remoteId)
    }

    fun delete(product: Product) = viewModelScope.launch {
        repository.deleteProductData(product)
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }

        throw IllegalAccessException("Unknown ViewModel Class")
    }
}