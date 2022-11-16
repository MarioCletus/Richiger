package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Operario
import kotlinx.coroutines.flow.Flow

@Dao
interface OperarioDao {
    @Insert
    suspend fun insertOperario(operario: Operario) : Long

    @Query("SELECT * FROM operario ORDER BY name")
    fun getAllOperarioList(): Flow<MutableList<Operario>>

    @Query("SELECT * FROM operario WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveOperarioList(): Flow<List<Operario>>

    @Query("SELECT * FROM operario WHERE archive_date IS NULL  OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredOperarioList(name: String): Flow<List<Operario>>

    @Query("SELECT * FROM operario WHERE id = :id")
    fun getOperarioById(id: Long): Flow<Operario>

    @Update
    suspend fun updateOperario(operario: Operario)

    @Query("UPDATE operario SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE operario SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteOperario(operario: Operario)
}