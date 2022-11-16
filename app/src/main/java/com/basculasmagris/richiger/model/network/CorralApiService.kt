package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Corral
import com.basculasmagris.richiger.model.entities.CorralRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CorralApiService {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(CorralAPI::class.java)

    fun getCorrals(): Single<MutableList<CorralRemote>> {
        return api.getCorrals()
    }

    fun addCorral(corral: Corral): Single<CorralRemote> {
        return api.addCorral(corral)
    }

    fun updateCorrals(corral: Corral): Single<CorralRemote> {
        return api.updateCorral(corral)
    }
}