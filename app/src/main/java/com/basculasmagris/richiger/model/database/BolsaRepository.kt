package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Bolsa
import kotlinx.coroutines.flow.Flow
import java.util.*

class BolsaRepository (private  val bolsaDao: BolsaDao) {

    @WorkerThread
    suspend fun insertBolsaData(bolsa: Bolsa) : Long{
        return bolsaDao.insertBolsa(bolsa)
    }

    val allBolsaList: Flow<MutableList<Bolsa>> = bolsaDao.getAllBolsaList()
    val allActiveList: Flow<List<Bolsa>> = bolsaDao.getActiveBolsaList()

    fun getBolsaById(id: Long) : Flow<Bolsa> = bolsaDao.getBolsaById(id)

    fun getFilteredBolsaList(value: String): Flow<List<Bolsa>> = bolsaDao.getFilteredBolsaList(value)


    @WorkerThread
    suspend fun updateBolsaData(bolsa: Bolsa){
        bolsaDao.updateBolsa(bolsa)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        bolsaDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        bolsaDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteBolsaData(bolsa: Bolsa){
        bolsaDao.deleteBolsa(bolsa)
    }
}

