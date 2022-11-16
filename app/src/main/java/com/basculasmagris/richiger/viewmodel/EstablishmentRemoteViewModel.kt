package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.model.entities.EstablishmentRemote
import com.basculasmagris.richiger.model.network.EstablishmentApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class EstablishmentRemoteViewModel : ViewModel() {

    private val establishmentApiService = EstablishmentApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadEstablishment = MutableLiveData<Boolean>()
    var establishmentsResponse = MutableLiveData<MutableList<EstablishmentRemote>?>()
    val establishmentsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addEstablishmentsLoad = MutableLiveData<Boolean>()
    val addEstablishmentsResponse = MutableLiveData<EstablishmentRemote?>()
    val addEstablishmentErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateEstablishmentsLoad = MutableLiveData<Boolean>()
    val updateEstablishmentsResponse = MutableLiveData<EstablishmentRemote?>()
    val updateEstablishmentsErrorResponse = MutableLiveData<Boolean>()

    fun getEstablishmentsFromAPI() {
        loadEstablishment.value = true
        compositeDisposable.add(
            establishmentApiService.getEstablishments()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<EstablishmentRemote>>() {
                    override fun onSuccess(value: MutableList<EstablishmentRemote>?) {
                        establishmentsResponse.value = value
                        establishmentsLoadingError.value = false
                        loadEstablishment.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadEstablishment.value = false
                        establishmentsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addEstablishmentFromAPI(establishment: Establishment){
        addEstablishmentsLoad.value = true
        compositeDisposable.add(
            establishmentApiService.addEstablishment(establishment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EstablishmentRemote>() {
                    override fun onSuccess(value: EstablishmentRemote?) {
                        addEstablishmentsResponse.value = value
                        addEstablishmentErrorResponse.value = false
                        addEstablishmentsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addEstablishmentsLoad.value = false
                        addEstablishmentErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateEstablishmentFromAPI(establishment: Establishment){
        updateEstablishmentsLoad.value = true
        compositeDisposable.add(
            establishmentApiService.updateEstablishments(establishment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EstablishmentRemote>() {
                    override fun onSuccess(value: EstablishmentRemote?) {
                        updateEstablishmentsResponse.value = value
                        updateEstablishmentsErrorResponse.value = false
                        updateEstablishmentsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateEstablishmentsLoad.value = false
                        updateEstablishmentsErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}