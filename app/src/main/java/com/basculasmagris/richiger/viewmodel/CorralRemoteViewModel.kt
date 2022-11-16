package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.CorralRemote
import com.basculasmagris.richiger.model.network.CorralApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CorralRemoteViewModel : ViewModel() {

    private val corralApiService = CorralApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadCorral = MutableLiveData<Boolean>()
    val corralsResponse = MutableLiveData<MutableList<CorralRemote>?>()
    val corralsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addCorralsLoad = MutableLiveData<Boolean>()
    val addCorralsResponse = MutableLiveData<CorralRemote?>()
    val addCorralErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateCorralsLoad = MutableLiveData<Boolean>()
    val updateCorralsResponse = MutableLiveData<CorralRemote?>()
    val updateCorralsErrorResponse = MutableLiveData<Boolean>()

    fun getCorralsFromAPI() {
        loadCorral.value = true
        compositeDisposable.add(
            corralApiService.getCorrals()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<CorralRemote>>() {
                    override fun onSuccess(value: MutableList<CorralRemote>?) {
                        loadCorral.value = false
                        corralsResponse.value = value
                        corralsLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadCorral.value = false
                        corralsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addCorralFromAPI(corral: Corral){
        addCorralsLoad.value = true
        compositeDisposable.add(
            corralApiService.addCorral(corral)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CorralRemote>() {
                    override fun onSuccess(value: CorralRemote?) {
                        addCorralsResponse.value = value
                        addCorralErrorResponse.value = false
                        addCorralsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addCorralsLoad.value = false
                        addCorralErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateCorralFromAPI(corral: Corral){
        updateCorralsLoad.value = true
        compositeDisposable.add(
            corralApiService.updateCorrals(corral)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CorralRemote>() {
                    override fun onSuccess(value: CorralRemote?) {
                        updateCorralsResponse.value = value
                        updateCorralsErrorResponse.value = false
                        updateCorralsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateCorralsLoad.value = false
                        updateCorralsErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}