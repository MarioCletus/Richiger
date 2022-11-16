package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Picadora
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PicadoraDao {
    @Insert
    suspend fun insertPicadora(picadora: Picadora) : Long

    @Query("SELECT * FROM picadora WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllPicadoraList(): Flow<MutableList<Picadora>>

    @Query("SELECT * FROM picadora ORDER BY name")
    fun getActivePicadoraList(): Flow<List<Picadora>>

    @Query("SELECT * FROM picadora WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredPicadoraList(name: String): Flow<List<Picadora>>

    @Query("SELECT * FROM picadora WHERE id = :id")
    fun getPicadoraById(id: Long): Flow<Picadora>


    @Update
    suspend fun updatePicadora(picadora: Picadora)

    @Query("UPDATE picadora SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE picadora SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deletePicadora(picadora: Picadora)
}