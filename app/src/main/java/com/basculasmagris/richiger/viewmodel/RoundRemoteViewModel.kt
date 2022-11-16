package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.richiger.model.entities.Round
import com.basculasmagris.richiger.model.entities.RoundRemote
import com.basculasmagris.richiger.model.network.RoundApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RoundRemoteViewModel : ViewModel() {

    private val roundApiService = RoundApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadRound = MutableLiveData<Boolean>()
    var roundsResponse = MutableLiveData<MutableList<RoundRemote>?>()
    val roundsLoadingError = MutableLiveData<Boolean>()

    //Post
    val addRoundsLoad = MutableLiveData<Boolean>()
    val addRoundsResponse = MutableLiveData<RoundRemote?>()
    val addRoundErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateRoundsLoad = MutableLiveData<Boolean>()
    val updateRoundsResponse = MutableLiveData<RoundRemote?>()
    val updateRoundsErrorResponse = MutableLiveData<Boolean>()

    fun getRoundsFromAPI() {
        loadRound.value = true
        compositeDisposable.add(
            roundApiService.getRounds()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<RoundRemote>>() {
                    override fun onSuccess(value: MutableList<RoundRemote>?) {
                        roundsResponse.value = value
                        roundsLoadingError.value = false
                        loadRound.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadRound.value = false
                        roundsLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addRoundFromAPI(round: RoundRemote){
        addRoundsLoad.value = true
        compositeDisposable.add(
            roundApiService.addRound(round)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RoundRemote>() {
                    override fun onSuccess(value: RoundRemote?) {
                        addRoundsResponse.value = value
                        addRoundErrorResponse.value = false
                        addRoundsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addRoundsLoad.value = false
                        addRoundErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateRoundFromAPI(round: RoundRemote){
        updateRoundsLoad.value = true
        compositeDisposable.add(
            roundApiService.updateRounds(round)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RoundRemote>() {
                    override fun onSuccess(value: RoundRemote?) {
                        updateRoundsResponse.value = value
                        updateRoundsErrorResponse.value = false
                        updateRoundsLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateRoundsLoad.value = false
                        updateRoundsErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}