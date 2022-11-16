package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Embolsadora
import kotlinx.coroutines.flow.Flow
import java.util.*

class EmbolsadoraRepository (private  val embolsadoraDao: EmbolsadoraDao) {

    @WorkerThread
    suspend fun insertEmbolsadoraData(embolsadora: Embolsadora) : Long{
        return embolsadoraDao.insertEmbolsadora(embolsadora)
    }

    val allEmbolsadoraList: Flow<MutableList<Embolsadora>> = embolsadoraDao.getAllEmbolsadoraList()
    val allActiveList: Flow<List<Embolsadora>> = embolsadoraDao.getActiveEmbolsadoraList()

    fun getEmbolsadoraById(id: Long) : Flow<Embolsadora> = embolsadoraDao.getEmbolsadoraById(id)

    fun getFilteredEmbolsadoraList(value: String): Flow<List<Embolsadora>> = embolsadoraDao.getFilteredEmbolsadoraList(value)


    @WorkerThread
    suspend fun updateEmbolsadoraData(embolsadora: Embolsadora){
        embolsadoraDao.updateEmbolsadora(embolsadora)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        embolsadoraDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        embolsadoraDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteEmbolsadoraData(embolsadora: Embolsadora){
        embolsadoraDao.deleteEmbolsadora(embolsadora)
    }
}

