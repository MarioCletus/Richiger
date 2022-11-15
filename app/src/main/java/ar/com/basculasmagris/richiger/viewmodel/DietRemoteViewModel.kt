package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.richiger.model.entities.Diet
import com.basculasmagris.richiger.model.entities.DietRemote
import com.basculasmagris.richiger.model.network.DietApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class DietRemoteViewModel : ViewModel() {

    private val dietApiService = DietApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadDiet = MutableLiveData<Boolean>()
    var dietsResponse = MutableLiveData<MutableList<DietRemote>?>()
    val dietsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addDietsLoad = MutableLiveData<Boolean>()
    val addDietsResponse = MutableLiveData<DietRemote?>()
    val addDietErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateDietsLoad = MutableLiveData<Boolean>()
    val updateDietsResponse = MutableLiveData<DietRemote?>()
    val updateDietsErrorResponse = MutableLiveData<Boolean>()

    fun getDietsFromAPI() {
        loadDiet.value = true
        compositeDisposable.add(
            dietApiService.getDiets()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<DietRemote>>() {
                    override fun onSuccess(value: MutableList<DietRemote>?) {
                        dietsResponse.value = value
                        dietsLoadingError.value = false
                        loadDiet.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadDiet.value = false
                        dietsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addDietFromAPI(diet: DietRemote){
        addDietsLoad.value = true
        compositeDisposable.add(
            dietApiService.addDiet(diet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DietRemote>() {
                    override fun onSuccess(value: DietRemote?) {
                        addDietsResponse.value = value
                        addDietErrorResponse.value = false
                        addDietsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addDietsLoad.value = false
                        addDietErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateDietFromAPI(diet: DietRemote){
        updateDietsLoad.value = true
        compositeDisposable.add(
            dietApiService.updateDiets(diet)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DietRemote>() {
                    override fun onSuccess(value: DietRemote?) {
                        updateDietsResponse.value = value
                        updateDietsErrorResponse.value = false
                        updateDietsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateDietsLoad.value = false
                        updateDietsErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}