package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Bolsa
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface BolsaDao {
    @Insert
    suspend fun insertBolsa(bolsa: Bolsa) : Long

    @Query("SELECT * FROM bolsa WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllBolsaList(): Flow<MutableList<Bolsa>>

    @Query("SELECT * FROM bolsa ORDER BY name")
    fun getActiveBolsaList(): Flow<List<Bolsa>>

    @Query("SELECT * FROM bolsa WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredBolsaList(name: String): Flow<List<Bolsa>>

    @Query("SELECT * FROM bolsa WHERE id = :id")
    fun getBolsaById(id: Long): Flow<Bolsa>


    @Update
    suspend fun updateBolsa(bolsa: Bolsa)

    @Query("UPDATE bolsa SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE bolsa SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteBolsa(bolsa: Bolsa)
}