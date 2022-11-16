package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Embolsado
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EmbolsadoDao {
    @Insert
    suspend fun insertEmbolsado(embolsado: Embolsado) : Long

    @Query("SELECT * FROM embolsado WHERE archive_date IS NULL OR archive_date = ''")
    fun getAllEmbolsadoList(): Flow<MutableList<Embolsado>>

    @Query("SELECT * FROM embolsado")
    fun getActiveEmbolsadoList(): Flow<List<Embolsado>>

//    @Query("SELECT * FROM embolsado WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
//    fun getFilteredEmbolsadoList(name: String): Flow<List<Embolsado>>

    @Query("SELECT * FROM embolsado WHERE id = :id")
    fun getEmbolsadoById(id: Long): Flow<Embolsado>


    @Update
    suspend fun updateEmbolsado(embolsado: Embolsado)

    @Query("UPDATE embolsado SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE embolsado SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteEmbolsado(embolsado: Embolsado)
}