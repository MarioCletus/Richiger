package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Picadora
import kotlinx.coroutines.flow.Flow
import java.util.*

class PicadoraRepository (private  val picadoraDao: PicadoraDao) {

    @WorkerThread
    suspend fun insertPicadoraData(picadora: Picadora) : Long{
        return picadoraDao.insertPicadora(picadora)
    }

    val allPicadoraList: Flow<MutableList<Picadora>> = picadoraDao.getAllPicadoraList()
    val allActiveList: Flow<List<Picadora>> = picadoraDao.getActivePicadoraList()

    fun getPicadoraById(id: Long) : Flow<Picadora> = picadoraDao.getPicadoraById(id)

    fun getFilteredPicadoraList(value: String): Flow<List<Picadora>> = picadoraDao.getFilteredPicadoraList(value)


    @WorkerThread
    suspend fun updatePicadoraData(picadora: Picadora){
        picadoraDao.updatePicadora(picadora)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        picadoraDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        picadoraDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deletePicadoraData(picadora: Picadora){
        picadoraDao.deletePicadora(picadora)
    }
}

