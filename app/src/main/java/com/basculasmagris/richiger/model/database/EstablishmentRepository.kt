package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Establishment
import kotlinx.coroutines.flow.Flow

class EstablishmentRepository (private  val establishmentDao: EstablishmentDao) {

    @WorkerThread
    suspend fun insertEstablishmentData(establishment: Establishment) : Long {
        return establishmentDao.insertEstablishment(establishment)
    }

    val allEstablishmentList: Flow<MutableList<Establishment>> = establishmentDao.getAllEstablishmentList()
    val activeEstablishmentList: Flow<List<Establishment>> = establishmentDao.getActiveEstablishmentList()
    fun getEstablishmentById(id: Long) : Flow<Establishment> = establishmentDao.getEstablishmentById(id)

    fun getFilteredEstablishmentList(value: String): Flow<List<Establishment>> = establishmentDao.getFilteredEstablishmentList(value)


    @WorkerThread
    suspend fun updateEstablishmentData(establishment: Establishment){
        establishmentDao.updateEstablishment(establishment)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        establishmentDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        establishmentDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteEstablishmentData(establishment: Establishment){
        establishmentDao.deleteEstablishment(establishment)
    }
}