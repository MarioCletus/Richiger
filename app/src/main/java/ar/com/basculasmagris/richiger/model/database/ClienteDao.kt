package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert
    suspend fun insertCliente(cliente: Cliente) : Long

    @Query("SELECT * FROM cliente ORDER BY name")
    fun getAllClienteList(): Flow<MutableList<Cliente>>

    @Query("SELECT * FROM cliente WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveClienteList(): Flow<List<Cliente>>

    @Query("SELECT * FROM cliente WHERE archive_date IS NULL  OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredClienteList(name: String): Flow<List<Cliente>>

    @Query("SELECT * FROM cliente WHERE id = :id")
    fun getClienteById(id: Int): Flow<Cliente>

    @Update
    suspend fun updateCliente(cliente: Cliente)

    @Query("UPDATE cliente SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE cliente SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteCliente(cliente: Cliente)
}