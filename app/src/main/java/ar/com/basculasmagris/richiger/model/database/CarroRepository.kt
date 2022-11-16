package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Carro
import kotlinx.coroutines.flow.Flow

class CarroRepository (private  val carroDao: CarroDao) {

    @WorkerThread
    suspend fun insertCarroData(carro: Carro) : Long {
        return carroDao.insertCarro(carro)
    }

    val allCarroList: Flow<MutableList<Carro>> = carroDao.getAllCarroList()
    val activeCarroList: Flow<List<Carro>> = carroDao.getActiveCarroList()

    fun getCarroById(id: Int) : Flow<Carro> = carroDao.getCarroById(id)
    fun getFilteredCarroList(value: String): Flow<List<Carro>> = carroDao.getFilteredCarroList(value)


    @WorkerThread
    suspend fun updateCarroData(carro: Carro){
        carroDao.updateCarro(carro)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        carroDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        carroDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteCarroData(carro: Carro){
        carroDao.deleteCarro(carro)
    }
}