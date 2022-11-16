package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Corral
import kotlinx.coroutines.flow.Flow

class CorralRepository (private  val corralDao: CorralDao) {

    @WorkerThread
    suspend fun insertCorralData(corral: Corral) : Long {
        return corralDao.insertCorral(corral)
    }

    val allCorralList: Flow<MutableList<Corral>> = corralDao.getAllCorralList()
    val activeCorralList: Flow<List<Corral>> = corralDao.getActiveCorralList()

    fun getCorralById(id: Int) : Flow<Corral> = corralDao.getCorralById(id)
    fun getCorralByEstablishment(id: Long): Flow<Corral>  = corralDao.getCorralByEstablishment(id)
    fun getFilteredCorralList(value: String): Flow<List<Corral>> = corralDao.getFilteredCorralList(value)


    @WorkerThread
    suspend fun updateCorralData(corral: Corral){
        corralDao.updateCorral(corral)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        corralDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        corralDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun setUpdatedEstablishmentRemoteId(id: Long, remoteId: Long){
        corralDao.setUpdatedEstablishmentRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteCorralData(corral: Corral){
        corralDao.deleteCorral(corral)
    }
}