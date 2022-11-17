package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.*
import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.model.entities.CarroRemote
import com.basculasmagris.richiger.model.network.CarroApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CarroRemoteViewModel : ViewModel() {

    private val carroApiService = CarroApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadCarro = MutableLiveData<Boolean>()
    val carrosResponse = MutableLiveData<MutableList<CarroRemote>?>()
    val carrosLoadingError = MutableLiveData<Boolean>()

    //Post
    val addCarrosLoad = MutableLiveData<Boolean>()
    val addCarrosResponse = MutableLiveData<CarroRemote?>()
    val addCarroErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateCarrosLoad = MutableLiveData<Boolean>()
    val updateCarrosResponse = MutableLiveData<CarroRemote?>()
    val updateCarrosErrorResponse = MutableLiveData<Boolean>()

    fun getCarrosFromAPI() {
        loadCarro.value = true
        compositeDisposable.add(
            carroApiService.getCarros()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<CarroRemote>>() {
                    override fun onSuccess(value: MutableList<CarroRemote>?) {
                        loadCarro.value = false
                        carrosResponse.value = value
                        carrosLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadCarro.value = false
                        carrosLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addCarroFromAPI(carro: Carro){
        addCarrosLoad.value = true
        compositeDisposable.add(
            carroApiService.addCarro(carro)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CarroRemote>() {
                    override fun onSuccess(value: CarroRemote?) {
                        addCarrosResponse.value = value
                        addCarroErrorResponse.value = false
                        addCarrosLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addCarrosLoad.value = false
                        addCarroErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateCarroFromAPI(carro: Carro){
        updateCarrosLoad.value = true
        compositeDisposable.add(
            carroApiService.updateCarros(carro)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CarroRemote>() {
                    override fun onSuccess(value: CarroRemote?) {
                        updateCarrosResponse.value = value
                        updateCarrosErrorResponse.value = false
                        updateCarrosLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateCarrosLoad.value = false
                        updateCarrosErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}