package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Embolsado
import kotlinx.coroutines.flow.Flow
import java.util.*

class EmbolsadoRepository (private  val embolsadoDao: EmbolsadoDao) {

    @WorkerThread
    suspend fun insertEmbolsadoData(embolsado: Embolsado) : Long{
        return embolsadoDao.insertEmbolsado(embolsado)
    }

    val allEmbolsadoList: Flow<MutableList<Embolsado>> = embolsadoDao.getAllEmbolsadoList()
    val allActiveList: Flow<List<Embolsado>> = embolsadoDao.getActiveEmbolsadoList()

    fun getEmbolsadoById(id: Long) : Flow<Embolsado> = embolsadoDao.getEmbolsadoById(id)

//    fun getFilteredEmbolsadoList(value: String): Flow<List<Embolsado>> = embolsadoDao.getFilteredEmbolsadoList(value)


    @WorkerThread
    suspend fun updateEmbolsadoData(embolsado: Embolsado){
        embolsadoDao.updateEmbolsado(embolsado)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        embolsadoDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        embolsadoDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteEmbolsadoData(embolsado: Embolsado){
        embolsadoDao.deleteEmbolsado(embolsado)
    }
}

