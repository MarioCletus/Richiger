package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Direccion
import kotlinx.coroutines.flow.Flow
import java.util.*

class DireccionRepository (private  val direccionDao: DireccionDao) {

    @WorkerThread
    suspend fun insertDireccionData(direccion: Direccion) : Long{
        return direccionDao.insertDireccion(direccion)
    }

    val allDireccionList: Flow<MutableList<Direccion>> = direccionDao.getAllDireccionList()
    val allActiveList: Flow<List<Direccion>> = direccionDao.getActiveDireccionList()

    fun getDireccionById(id: Long) : Flow<Direccion> = direccionDao.getDireccionById(id)

    fun getFilteredDireccionList(value: String): Flow<List<Direccion>> = direccionDao.getFilteredDireccionList(value)


    @WorkerThread
    suspend fun updateDireccionData(direccion: Direccion){
        direccionDao.updateDireccion(direccion)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        direccionDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        direccionDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteDireccionData(direccion: Direccion){
        direccionDao.deleteDireccion(direccion)
    }
}

