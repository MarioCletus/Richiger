package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Establishment
import kotlinx.coroutines.flow.Flow

@Dao
interface EstablishmentDao {
    @Insert
    suspend fun insertEstablishment(establishment: Establishment) : Long

    @Query("SELECT * FROM establishment WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveEstablishmentList(): Flow<List<Establishment>>

    @Query("SELECT * FROM establishment ORDER BY name")
    fun getAllEstablishmentList(): Flow<MutableList<Establishment>>

    @Query("SELECT * FROM establishment WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredEstablishmentList(name: String): Flow<List<Establishment>>

    @Query("SELECT * FROM establishment WHERE id = :id")
    fun getEstablishmentById(id: Long): Flow<Establishment>

    @Update
    suspend fun updateEstablishment(establishment: Establishment)

    @Query("UPDATE establishment SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE establishment SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteEstablishment(establishment: Establishment)
}