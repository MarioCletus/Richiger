package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.model.entities.ProductRemote
import com.basculasmagris.richiger.model.network.ProductApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class ProductRemoteViewModel : ViewModel() {

    private val productApiService = ProductApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadProduct = MutableLiveData<Boolean>()
    val productsResponse = MutableLiveData<MutableList<ProductRemote>?>()
    val productsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addProductsLoad = MutableLiveData<Boolean>()
    val addProductsResponse = MutableLiveData<ProductRemote?>()
    val addProductErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateProductsLoad = MutableLiveData<Boolean>()
    val updateProductsResponse = MutableLiveData<ProductRemote?>()
    val updateProductsErrorResponse = MutableLiveData<Boolean>()

    fun getProductsFromAPI() {
        loadProduct.value = true
        compositeDisposable.add(
            productApiService.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<ProductRemote>>() {
                    override fun onSuccess(value: MutableList<ProductRemote>?) {
                        loadProduct.value = false
                        productsResponse.value = value
                        productsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadProduct.value = false
                        productsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addProductFromAPI(product: Product){
        addProductsLoad.value = true
        compositeDisposable.add(
            productApiService.addProduct(product)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProductRemote>() {
                    override fun onSuccess(value: ProductRemote?) {
                        addProductsResponse.value = value
                        addProductErrorResponse.value = false
                        addProductsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addProductsLoad.value = false
                        addProductErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateProductFromAPI(product: Product){
        updateProductsLoad.value = true
        compositeDisposable.add(
            productApiService.updateProducts(product)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProductRemote>() {
                    override fun onSuccess(value: ProductRemote?) {
                        updateProductsResponse.value = value
                        updateProductsErrorResponse.value = false
                        updateProductsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateProductsLoad.value = false
                        updateProductsErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}