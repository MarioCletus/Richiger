package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Carro
import kotlinx.coroutines.flow.Flow

@Dao
interface CarroDao {
    @Insert
    suspend fun insertCarro(carro: Carro) : Long

    @Query("SELECT * FROM carro ORDER BY name")
    fun getAllCarroList(): Flow<MutableList<Carro>>

    @Query("SELECT * FROM carro WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveCarroList(): Flow<List<Carro>>

    @Query("SELECT * FROM carro WHERE archive_date IS NULL  OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredCarroList(name: String): Flow<List<Carro>>

    @Query("SELECT * FROM carro WHERE id = :id")
    fun getCarroById(id: Int): Flow<Carro>

    @Update
    suspend fun updateCarro(carro: Carro)

    @Query("UPDATE carro SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE carro SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteCarro(carro: Carro)
}