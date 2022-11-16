package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Direccion
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DireccionDao {
    @Insert
    suspend fun insertDireccion(direccion: Direccion) : Long

    @Query("SELECT * FROM direccion WHERE archive_date IS NULL OR archive_date = '' ORDER BY calle")
    fun getAllDireccionList(): Flow<MutableList<Direccion>>

    @Query("SELECT * FROM direccion ORDER BY calle")
    fun getActiveDireccionList(): Flow<List<Direccion>>

    @Query("SELECT * FROM direccion WHERE archive_date IS NULL OR archive_date = '' ORDER BY calle LIKE :calle")
    fun getFilteredDireccionList(calle: String): Flow<List<Direccion>>

    @Query("SELECT * FROM direccion WHERE id = :id")
    fun getDireccionById(id: Long): Flow<Direccion>


    @Update
    suspend fun updateDireccion(direccion: Direccion)

    @Query("UPDATE direccion SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE direccion SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteDireccion(direccion: Direccion)
}