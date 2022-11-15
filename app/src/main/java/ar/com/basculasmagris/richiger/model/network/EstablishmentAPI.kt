package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Establishment
import com.basculasmagris.richiger.model.entities.EstablishmentRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface EstablishmentAPI {
    @GET("${Constants.API_ESTABLISHMENT_ENDPOINT}/all/")
    fun getEstablishments() : Single<MutableList<EstablishmentRemote>>

    @POST(Constants.API_ESTABLISHMENT_ENDPOINT)
    fun addEstablishment(@Body establishment: Establishment) : Single<EstablishmentRemote>

    @PUT(Constants.API_ESTABLISHMENT_ENDPOINT)
    fun updateEstablishment(@Body establishment: Establishment) : Single<EstablishmentRemote>
}