package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Diet
import com.basculasmagris.richiger.model.entities.DietProduct
import com.basculasmagris.richiger.model.entities.DietProductDetail
import kotlinx.coroutines.flow.Flow

class DietRepository (private  val dietDao: DietDao) {

    @WorkerThread
    suspend fun insertDietData(diet: Diet) : Long {
        return dietDao.insertDiet(diet)
    }

    /*
    @WorkerThread
    suspend fun insertSyncDietData(diet: Diet): Long {
        return dietDao.insertDiet(diet)
    }
    */

    @WorkerThread
    suspend fun insertDietProductData(dietProduct: DietProduct){
        dietDao.insertDietProduct(dietProduct)
    }

    val allDietList: Flow<MutableList<Diet>> = dietDao.getAllDietList()
    val activeDietList: Flow<MutableList<Diet>> = dietDao.getActiveDietList()
    fun getDietById(id: Long) : Flow<Diet> = dietDao.getDietById(id)

    fun getFilteredDietList(value: String): Flow<List<Diet>> = dietDao.getFilteredDietList(value)


    @WorkerThread
    suspend fun updateDietData(diet: Diet){
        dietDao.updateDiet(diet)
    }

    @WorkerThread
    suspend fun updateDietProductData(dietId: Long, productId: Long, order: Int, weight: Double, percentage: Double){
        dietDao.updateDietProduct(dietId, productId, order, weight, percentage)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        dietDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        dietDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteDietData(diet: Diet){
        dietDao.deleteDiet(diet)
    }

    @WorkerThread
    suspend fun deleteDietProductData(dietId: Long, productId: Long){
        dietDao.deleteDiet(dietId, productId)
    }

    @WorkerThread
    suspend fun deleteProductsByDietData(dietId: Long){
        dietDao.deleteProductsByDiet(dietId)
    }

    fun getProductsBy(idDiet: Long) : Flow<MutableList<DietProductDetail>> = dietDao.getProductsBy(idDiet)

    @WorkerThread
    fun getSyncProductsBy(idDiet: Long) : List<DietProductDetail> = dietDao.getSyncProductsBy(idDiet)

    val getAllDietProductList: Flow<MutableList<DietProduct>> = dietDao.getAllDietProductList()

}