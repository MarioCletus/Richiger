package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.RoundRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoundAPI {
    @GET("${Constants.API_ROUND_ENDPOINT}/all/")
    fun getRounds() : Single<MutableList<RoundRemote>>

    @POST(Constants.API_ROUND_ENDPOINT)
    fun addRound(@Body round: RoundRemote) : Single<RoundRemote>

    @PUT(Constants.API_ROUND_ENDPOINT)
    fun updateRound(@Body round: RoundRemote) : Single<RoundRemote>
}