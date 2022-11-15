package com.basculasmagris.richiger.model.network

import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.model.entities.ProductRemote
import com.basculasmagris.richiger.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProductApiService {
    private val api = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ProductAPI::class.java)

    fun getProducts(): Single<MutableList<ProductRemote>> {
        return api.getProducts()
    }

    fun addProduct(product: Product): Single<ProductRemote> {
        return api.addProduct(product)
    }

    fun updateProducts(product: Product): Single<ProductRemote> {
        return api.updateProduct(product)
    }
}