package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Descarga
import kotlinx.coroutines.flow.Flow

@Dao
interface DescargaDao {
    @Insert
    suspend fun insertDescarga(descarga: Descarga) : Long

    @Query("SELECT * FROM descarga")
    fun getAllDescargaList(): Flow<MutableList<Descarga>>

    @Query("SELECT * FROM descarga WHERE archive_date IS NULL OR archive_date = ''")
    fun getActiveDescargaList(): Flow<List<Descarga>>

    @Query("SELECT * FROM descarga WHERE archive_date IS NULL  OR archive_date = ''")
    fun getFilteredDescargaList(name: String): Flow<List<Descarga>>

    @Query("SELECT * FROM descarga WHERE id = :id")
    fun getDescargaById(id: Int): Flow<Descarga>

    @Update
    suspend fun updateDescarga(descarga: Descarga)

    @Query("UPDATE descarga SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE descarga SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteDescarga(descarga: Descarga)
}