package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Corral
import kotlinx.coroutines.flow.Flow

@Dao
interface CorralDao {
    @Insert
    suspend fun insertCorral(corral: Corral) : Long

    @Query("SELECT * FROM corral ORDER BY name")
    fun getAllCorralList(): Flow<MutableList<Corral>>

    @Query("SELECT * FROM corral WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveCorralList(): Flow<List<Corral>>

    @Query("SELECT * FROM corral WHERE archive_date IS NULL  OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredCorralList(name: String): Flow<List<Corral>>

    @Query("SELECT * FROM corral WHERE id = :id")
    fun getCorralById(id: Int): Flow<Corral>

    @Query("SELECT * FROM corral WHERE establishment_id = :id")
    fun getCorralByEstablishment(id: Long): Flow<Corral>

    @Update
    suspend fun updateCorral(corral: Corral)

    @Query("UPDATE corral SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE corral SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Query("UPDATE corral SET establishment_remote_id = :establishmentRemoteId WHERE id = :id")
    suspend fun setUpdatedEstablishmentRemoteId(id: Long, establishmentRemoteId: Long)

    @Delete
    suspend fun deleteCorral(corral: Corral)
}