package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Localidad
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface LocalidadDao {
    @Insert
    suspend fun insertLocalidad(localidad: Localidad) : Long

    @Query("SELECT * FROM localidad WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllLocalidadList(): Flow<MutableList<Localidad>>

    @Query("SELECT * FROM localidad ORDER BY name")
    fun getActiveLocalidadList(): Flow<List<Localidad>>

    @Query("SELECT * FROM localidad WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredLocalidadList(name: String): Flow<List<Localidad>>

    @Query("SELECT * FROM localidad WHERE id = :id")
    fun getLocalidadById(id: Long): Flow<Localidad>


    @Update
    suspend fun updateLocalidad(localidad: Localidad)

    @Query("UPDATE localidad SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE localidad SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteLocalidad(localidad: Localidad)
}