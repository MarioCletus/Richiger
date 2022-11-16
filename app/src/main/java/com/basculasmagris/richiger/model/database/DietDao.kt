package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Diet
import com.basculasmagris.richiger.model.entities.DietProduct
import com.basculasmagris.richiger.model.entities.DietProductDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface DietDao {
    @Insert
    suspend fun insertDiet(diet: Diet): Long

    /*
    @Insert
    suspend fun insertSyncDiet(diet: Diet): Long
    */

    @Query("SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveDietList(): Flow<MutableList<Diet>>

    @Query("SELECT * FROM diet ORDER BY id DESC")
    fun getAllDietList(): Flow<MutableList<Diet>>

    @Query("SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredDietList(name: String): Flow<List<Diet>>

    @Query("SELECT * FROM diet WHERE id = :id")
    fun getDietById(id: Long): Flow<Diet>

    @Update
    suspend fun updateDiet(diet: Diet)

    @Query("UPDATE diet SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE diet SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteDiet(diet: Diet)

    @Query("DELETE FROM diet_product WHERE diet_id = :dietId AND product_id = :productId")
    suspend fun deleteDiet(dietId: Long, productId: Long)

    @Query("DELETE FROM diet_product WHERE diet_id = :dietId")
    suspend fun deleteProductsByDiet(dietId: Long)


    // DietProduct
    @Query("SELECT dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = :idDiet ORDER BY dp.`order`")
    fun getProductsBy(idDiet: Long): Flow<MutableList<DietProductDetail>>

    // DietProduct
    @Query("SELECT  dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = :idDiet ORDER BY dp.`order`")
    fun getSyncProductsBy(idDiet: Long): List<DietProductDetail>

    @Query("SELECT * FROM diet_product as dp ORDER BY dp.`order`")
    fun getAllDietProductList(): Flow<MutableList<DietProduct>>

    @Insert
    suspend fun insertDietProduct(dietProduct: DietProduct)

    @Query("UPDATE diet_product SET `order` = :order, weight = :weight, percentage = :percentage WHERE diet_id = :dietId AND product_id = :productId")
    suspend fun updateDietProduct(dietId: Long, productId: Long, order: Int, weight: Double, percentage: Double)
}