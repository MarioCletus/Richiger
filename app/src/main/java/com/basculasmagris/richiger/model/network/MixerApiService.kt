package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Mixer
import com.basculasmagris.richiger.model.entities.MixerRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MixerApiService  {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(MixerAPI::class.java)

    fun getMixers(): Single<MutableList<MixerRemote>> {
        return api.getMixers()
    }

    fun addMixer(mixer: Mixer): Single<MixerRemote> {
        return api.addMixer(mixer)
    }

    fun updateMixers(mixer: Mixer): Single<MixerRemote> {
        return api.updateMixer(mixer)
    }
}