package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Carro
import com.basculasmagris.richiger.model.entities.CarroRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CarroApiService {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(CarroAPI::class.java)

    fun getCarros(): Single<MutableList<CarroRemote>> {
        return api.getCarros()
    }

    fun addCarro(carro: Carro): Single<CarroRemote> {
        return api.addCarro(carro)
    }

    fun updateCarros(carro: Carro): Single<CarroRemote> {
        return api.updateCarro(carro)
    }
}