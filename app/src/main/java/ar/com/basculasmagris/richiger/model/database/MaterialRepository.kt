package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Material
import kotlinx.coroutines.flow.Flow
import java.util.*

class MaterialRepository (private  val materialDao: MaterialDao) {

    @WorkerThread
    suspend fun insertMaterialData(material: Material) : Long{
        return materialDao.insertMaterial(material)
    }

    val allMaterialList: Flow<MutableList<Material>> = materialDao.getAllMaterialList()
    val allActiveList: Flow<List<Material>> = materialDao.getActiveMaterialList()

    fun getMaterialById(id: Long) : Flow<Material> = materialDao.getMaterialById(id)

    fun getFilteredMaterialList(value: String): Flow<List<Material>> = materialDao.getFilteredMaterialList(value)


    @WorkerThread
    suspend fun updateMaterialData(material: Material){
        materialDao.updateMaterial(material)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        materialDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        materialDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteMaterialData(material: Material){
        materialDao.deleteMaterial(material)
    }
}

