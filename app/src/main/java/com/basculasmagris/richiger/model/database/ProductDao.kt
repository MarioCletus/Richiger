package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Product
import com.basculasmagris.richiger.model.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product) : Long

    @Query("SELECT * FROM product WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getAllProductList(): Flow<MutableList<Product>>

    @Query("SELECT * FROM product ORDER BY name")
    fun getActiveProductList(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredProductList(name: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Long): Flow<Product>


    @Update
    suspend fun updateProduct(product: Product)

    @Query("UPDATE product SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE product SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteProduct(product: Product)
}