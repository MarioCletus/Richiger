package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Descarga
import kotlinx.coroutines.flow.Flow

class DescargaRepository (private  val descargaDao: DescargaDao) {

    @WorkerThread
    suspend fun insertDescargaData(descarga: Descarga) : Long {
        return descargaDao.insertDescarga(descarga)
    }

    val allDescargaList: Flow<MutableList<Descarga>> = descargaDao.getAllDescargaList()
    val activeDescargaList: Flow<List<Descarga>> = descargaDao.getActiveDescargaList()

    fun getDescargaById(id: Int) : Flow<Descarga> = descargaDao.getDescargaById(id)
    fun getFilteredDescargaList(value: String): Flow<List<Descarga>> = descargaDao.getFilteredDescargaList(value)


    @WorkerThread
    suspend fun updateDescargaData(descarga: Descarga){
        descargaDao.updateDescarga(descarga)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        descargaDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        descargaDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteDescargaData(descarga: Descarga){
        descargaDao.deleteDescarga(descarga)
    }
}