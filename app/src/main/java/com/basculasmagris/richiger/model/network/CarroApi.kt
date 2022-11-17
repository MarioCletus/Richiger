package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.model.entities.CarroRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface CarroAPI {
    @GET("${Constants.API_PRODUCT_ENDPOINT}/all/")
    fun getCarros() : Single<MutableList<CarroRemote>>

    @POST(Constants.API_CARRO_ENDPOINT)
    fun addCarro(@Body carro: Carro) : Single<CarroRemote>

    @PUT(Constants.API_CARRO_ENDPOINT)
    fun updateCarro(@Body carro: Carro) : Single<CarroRemote>
}