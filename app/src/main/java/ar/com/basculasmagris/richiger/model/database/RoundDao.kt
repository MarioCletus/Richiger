package com.basculasmagris.richiger.model.database

import androidx.room.*
import com.basculasmagris.richiger.model.entities.Round
import com.basculasmagris.richiger.model.entities.RoundCorral
import com.basculasmagris.richiger.model.entities.RoundCorralDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundDao {
    @Insert
    suspend fun insertRound(round: Round) : Long

    @Query("SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name")
    fun getActiveRoundList(): Flow<MutableList<Round>>

    @Query("SELECT * FROM round ORDER BY id DESC")
    fun getAllRoundList(): Flow<MutableList<Round>>

    @Query("SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE :name")
    fun getFilteredRoundList(name: String): Flow<List<Round>>

    @Query("SELECT * FROM round WHERE id = :id")
    fun getRoundById(id: Long): Flow<Round>

    @Update
    suspend fun updateRound(round: Round)

    @Query("UPDATE round SET updated_date = :date WHERE id = :id")
    suspend fun setUpdatedDate(id: Long, date: String)

    @Query("UPDATE round SET remote_id = :remoteId WHERE id = :id")
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long)

    @Delete
    suspend fun deleteRound(round: Round)

    @Query("DELETE FROM round_corral WHERE round_id = :roundId AND corral_id = :corralId")
    suspend fun deleteRound(roundId: Long, corralId: Long)

    @Query("DELETE FROM round_corral WHERE round_id = :roundId")
    suspend fun deleteCorralByRound(roundId: Long)



    // RoundCorral
    @Query("SELECT dp.remote_round_id as remoteRoundId, dp.remote_corral_id as remoteCorralId, dp.corral_id as corralId, p.name as corralName, p.description as corralDescription, dp.round_id as roundId,dp.percentage, dp.weight, dp.`order`, p.animal_quantity as animalQuantity FROM round_corral as dp JOIN corral as p ON p.id = dp.corral_id WHERE round_id = :idRound ORDER BY dp.`order`")
    //@Query("SELECT * FROM round_corral WHERE round_id = :idRound")
    fun getCorralsBy(idRound: Long): Flow<MutableList<RoundCorralDetail>>

    @Query("SELECT rc.`order`, rc.weight, rc.percentage, rc.remote_id, rc.archive_date, rc.corral_id, rc.remote_corral_id, rc.remote_round_id, rc.round_id, rc.updated_date, c.animal_quantity as animalQuantity FROM round_corral rc JOIN corral c ON c.id == rc.corral_id")
    fun getAllRoundCorralList(): Flow<MutableList<RoundCorral>>

    @Insert
    suspend fun insertRoundCorral(roundCorral: RoundCorral)

    @Query("UPDATE round_corral SET `order` = :order, weight = :weight, percentage = :percentage WHERE round_id = :roundId AND corral_id = :corralId")
    suspend fun updateRoundCorral(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double)
}