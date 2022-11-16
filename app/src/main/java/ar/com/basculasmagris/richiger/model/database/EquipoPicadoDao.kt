package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.EquipoPicado
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EquipoPicadoDao {
    @Insert
    suspend fun insertEquipoPicado(equipoPicado: EquipoPicado) : Long

    @Query("SELECT * FROM equipoPicado WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllEquipoPicadoList(): Flow<MutableList<EquipoPicado>>

    @Query("SELECT * FROM equipoPicado ORDER BY name")
    fun getActiveEquipoPicadoList(): Flow<List<EquipoPicado>>

    @Query("SELECT * FROM equipoPicado WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredEquipoPicadoList(name: String): Flow<List<EquipoPicado>>

    @Query("SELECT * FROM equipoPicado WHERE id = :id")
    fun getEquipoPicadoById(id: Long): Flow<EquipoPicado>


    @Update
    suspend fun updateEquipoPicado(equipoPicado: EquipoPicado)

    @Query("UPDATE equipoPicado SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE equipoPicado SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteEquipoPicado(equipoPicado: EquipoPicado)
}