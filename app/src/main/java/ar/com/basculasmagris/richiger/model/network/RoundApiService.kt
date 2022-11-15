package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Round
import com.basculasmagris.richiger.model.entities.RoundRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RoundApiService {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(RoundAPI::class.java)

    fun getRounds(): Single<MutableList<RoundRemote>> {
        return api.getRounds()
    }

    fun addRound(round: RoundRemote): Single<RoundRemote> {
        return api.addRound(round)
    }

    fun updateRounds(round: RoundRemote): Single<RoundRemote> {
        return api.updateRound(round)
    }
}