package com.basculasmagris.richiger.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.model.entities.MixerRemote
import com.basculasmagris.richiger.model.network.MixerApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MixerRemoteViewModel : ViewModel() {

    private val mixerApiService = MixerApiService()
    private val compositeDisposable = CompositeDisposable()

    // Get
    val loadMixer = MutableLiveData<Boolean>()
    val mixersResponse = MutableLiveData<MutableList<MixerRemote>?>()
    val mixersLoadingError = MutableLiveData<Boolean>()

    //Post
    val addMixersLoad = MutableLiveData<Boolean>()
    val addMixersResponse = MutableLiveData<MixerRemote?>()
    val addMixerErrorResponse = MutableLiveData<Boolean>()

    //Put
    val updateMixersLoad = MutableLiveData<Boolean>()
    val updateMixersResponse = MutableLiveData<MixerRemote?>()
    val updateMixersErrorResponse = MutableLiveData<Boolean>()

    fun getMixersFromAPI() {
        loadMixer.value = true
        compositeDisposable.add(
            mixerApiService.getMixers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MutableList<MixerRemote>>() {
                    override fun onSuccess(value: MutableList<MixerRemote>?) {
                        loadMixer.value = false
                        mixersResponse.value = value
                        mixersLoadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loadMixer.value = false
                        mixersLoadingError.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun addMixerFromAPI(mixer: Mixer){
        addMixersLoad.value = true
        compositeDisposable.add(
            mixerApiService.addMixer(mixer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MixerRemote>() {
                    override fun onSuccess(value: MixerRemote?) {
                        addMixersResponse.value = value
                        addMixerErrorResponse.value = false
                        addMixersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        addMixersLoad.value = false
                        addMixerErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

    fun updateMixerFromAPI(mixer: Mixer){
        updateMixersLoad.value = true
        compositeDisposable.add(
            mixerApiService.updateMixers(mixer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MixerRemote>() {
                    override fun onSuccess(value: MixerRemote?) {
                        updateMixersResponse.value = value
                        updateMixersErrorResponse.value = false
                        updateMixersLoad.value = false
                    }

                    override fun onError(e: Throwable?) {
                        updateMixersLoad.value = false
                        updateMixersErrorResponse.value = true
                        e!!.printStackTrace()
                    }
                }))
    }

}