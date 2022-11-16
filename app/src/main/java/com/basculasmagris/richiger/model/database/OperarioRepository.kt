package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Operario
import kotlinx.coroutines.flow.Flow
import java.util.*

class OperarioRepository (private  val operarioDao: OperarioDao) {

    @WorkerThread
    suspend fun insertOperarioData(operario: Operario) : Long{
        return operarioDao.insertOperario(operario)
    }

    val allOperarioList: Flow<MutableList<Operario>> = operarioDao.getAllOperarioList()
    val allActiveList: Flow<List<Operario>> = operarioDao.getActiveOperarioList()

    fun getOperarioById(id: Long) : Flow<Operario> = operarioDao.getOperarioById(id)

    fun getFilteredOperarioList(value: String): Flow<List<Operario>> = operarioDao.getFilteredOperarioList(value)


    @WorkerThread
    suspend fun updateOperarioData(operario: Operario){
        operarioDao.updateOperario(operario)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        operarioDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        operarioDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteOperarioData(operario: Operario){
        operarioDao.deleteOperario(operario)
    }
}

