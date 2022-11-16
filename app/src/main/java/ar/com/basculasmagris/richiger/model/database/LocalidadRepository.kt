package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Localidad
import kotlinx.coroutines.flow.Flow
import java.util.*

class LocalidadRepository (private  val localidadDao: LocalidadDao) {

    @WorkerThread
    suspend fun insertLocalidadData(localidad: Localidad) : Long{
        return localidadDao.insertLocalidad(localidad)
    }

    val allLocalidadList: Flow<MutableList<Localidad>> = localidadDao.getAllLocalidadList()
    val allActiveList: Flow<List<Localidad>> = localidadDao.getActiveLocalidadList()

    fun getLocalidadById(id: Long) : Flow<Localidad> = localidadDao.getLocalidadById(id)

    fun getFilteredLocalidadList(value: String): Flow<List<Localidad>> = localidadDao.getFilteredLocalidadList(value)


    @WorkerThread
    suspend fun updateLocalidadData(localidad: Localidad){
        localidadDao.updateLocalidad(localidad)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        localidadDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        localidadDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteLocalidadData(localidad: Localidad){
        localidadDao.deleteLocalidad(localidad)
    }
}

