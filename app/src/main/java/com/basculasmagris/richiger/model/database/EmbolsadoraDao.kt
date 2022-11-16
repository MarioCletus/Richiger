package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Embolsadora
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EmbolsadoraDao {
    @Insert
    suspend fun insertEmbolsadora(embolsadora: Embolsadora) : Long

    @Query("SELECT * FROM embolsadora WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllEmbolsadoraList(): Flow<MutableList<Embolsadora>>

    @Query("SELECT * FROM embolsadora ORDER BY name")
    fun getActiveEmbolsadoraList(): Flow<List<Embolsadora>>

    @Query("SELECT * FROM embolsadora WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredEmbolsadoraList(name: String): Flow<List<Embolsadora>>

    @Query("SELECT * FROM embolsadora WHERE id = :id")
    fun getEmbolsadoraById(id: Long): Flow<Embolsadora>


    @Update
    suspend fun updateEmbolsadora(embolsadora: Embolsadora)

    @Query("UPDATE embolsadora SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE embolsadora SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteEmbolsadora(embolsadora: Embolsadora)
}