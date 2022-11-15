package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Product
import kotlinx.coroutines.flow.Flow
import java.util.*

class ProductRepository (private  val productDao: ProductDao) {

    @WorkerThread
    suspend fun insertProductData(product: Product) : Long{
        return productDao.insertProduct(product)
    }

    val allProductList: Flow<MutableList<Product>> = productDao.getAllProductList()
    val allActiveList: Flow<List<Product>> = productDao.getActiveProductList()

    fun getProductById(id: Long) : Flow<Product> = productDao.getProductById(id)

    fun getFilteredProductList(value: String): Flow<List<Product>> = productDao.getFilteredProductList(value)


    @WorkerThread
    suspend fun updateProductData(product: Product){
        productDao.updateProduct(product)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        productDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        productDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteProductData(product: Product){
        productDao.deleteProduct(product)
    }
}

