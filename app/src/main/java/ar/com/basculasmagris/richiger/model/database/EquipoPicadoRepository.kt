package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.EquipoPicado
import kotlinx.coroutines.flow.Flow
import java.util.*

class EquipoPicadoRepository (private  val equipoPicadoDao: EquipoPicadoDao) {

    @WorkerThread
    suspend fun insertEquipoPicadoData(equipoPicado: EquipoPicado) : Long{
        return equipoPicadoDao.insertEquipoPicado(equipoPicado)
    }

    val allEquipoPicadoList: Flow<MutableList<EquipoPicado>> = equipoPicadoDao.getAllEquipoPicadoList()
    val allActiveList: Flow<List<EquipoPicado>> = equipoPicadoDao.getActiveEquipoPicadoList()

    fun getEquipoPicadoById(id: Long) : Flow<EquipoPicado> = equipoPicadoDao.getEquipoPicadoById(id)

    fun getFilteredEquipoPicadoList(value: String): Flow<List<EquipoPicado>> = equipoPicadoDao.getFilteredEquipoPicadoList(value)


    @WorkerThread
    suspend fun updateEquipoPicadoData(equipoPicado: EquipoPicado){
        equipoPicadoDao.updateEquipoPicado(equipoPicado)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        equipoPicadoDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        equipoPicadoDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteEquipoPicadoData(equipoPicado: EquipoPicado){
        equipoPicadoDao.deleteEquipoPicado(equipoPicado)
    }
}

