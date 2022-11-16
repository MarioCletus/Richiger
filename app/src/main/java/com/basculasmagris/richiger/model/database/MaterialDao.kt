package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Material
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface MaterialDao {
    @Insert
    suspend fun insertMaterial(material: Material) : Long

    @Query("SELECT * FROM material WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllMaterialList(): Flow<MutableList<Material>>

    @Query("SELECT * FROM material ORDER BY name")
    fun getActiveMaterialList(): Flow<List<Material>>

    @Query("SELECT * FROM material WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredMaterialList(name: String): Flow<List<Material>>

    @Query("SELECT * FROM material WHERE id = :id")
    fun getMaterialById(id: Long): Flow<Material>


    @Update
    suspend fun updateMaterial(material: Material)

    @Query("UPDATE material SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE material SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteMaterial(material: Material)
}