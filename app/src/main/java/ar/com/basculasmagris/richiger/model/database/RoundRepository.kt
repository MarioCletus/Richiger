package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Round
import com.basculasmagris.richiger.model.entities.RoundCorral
import com.basculasmagris.richiger.model.entities.RoundCorralDetail
import kotlinx.coroutines.flow.Flow

class RoundRepository (private  val roundDao: RoundDao) {

    @WorkerThread
    suspend fun insertRoundData(round: Round) : Long{
        return roundDao.insertRound(round)
    }

    @WorkerThread
    suspend fun insertRoundCorralData(roundCorral: RoundCorral){
        roundDao.insertRoundCorral(roundCorral)
    }

    val allRoundList: Flow<MutableList<Round>> = roundDao.getAllRoundList()
    val activeRoundList: Flow<MutableList<Round>> = roundDao.getActiveRoundList()
    fun getRoundById(id: Long) : Flow<Round> = roundDao.getRoundById(id)

    fun getFilteredRoundList(value: String): Flow<List<Round>> = roundDao.getFilteredRoundList(value)


    @WorkerThread
    suspend fun updateRoundData(round: Round){
        roundDao.updateRound(round)
    }

    @WorkerThread
    suspend fun updateRoundCorralData(roundId: Long, corralId: Long, order: Int, weight: Double, percentage: Double){
        roundDao.updateRoundCorral(roundId, corralId, order, weight, percentage)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        roundDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        roundDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteRoundData(round: Round){
        roundDao.deleteRound(round)
    }

    @WorkerThread
    suspend fun deleteRoundCorralData(roundId: Long, corralId: Long){
        roundDao.deleteRound(roundId, corralId)
    }

    @WorkerThread
    suspend fun deleteCorralByRoundData(roundId: Long){
        roundDao.deleteCorralByRound(roundId)
    }



    fun getCorralsBy(idRound: Long) : Flow<MutableList<RoundCorralDetail>> = roundDao.getCorralsBy(idRound)
    val getAllRoundCorralList: Flow<MutableList<RoundCorral>> = roundDao.getAllRoundCorralList()

}