package com.basculasmagris.richiger.model.database

import androidx.annotation.WorkerThread
import com.basculasmagris.richiger.model.entities.Mixer
import kotlinx.coroutines.flow.Flow

class MixerRepository (private  val mixerDao: MixerDao) {

    @WorkerThread
    suspend fun insertMixerData(mixer: Mixer) : Long {
        return mixerDao.insertMixer(mixer)
    }

    val allMixerList: Flow<MutableList<Mixer>> = mixerDao.getAllMixerList()
    val activeMixerList: Flow<List<Mixer>> = mixerDao.getActiveMixerList()

    fun getMixerById(id: Long) : Flow<Mixer> = mixerDao.getMixerById(id)

    fun getFilteredMixerList(value: String): Flow<List<Mixer>> = mixerDao.getFilteredMixerList(value)


    @WorkerThread
    suspend fun updateMixerData(mixer: Mixer){
        mixerDao.updateMixer(mixer)
    }

    @WorkerThread
    suspend fun setUpdatedDate(id: Long, date: String){
        mixerDao.setUpdatedDate(id, date)
    }

    @WorkerThread
    suspend fun setUpdatedRemoteId(id: Long, remoteId: Long){
        mixerDao.setUpdatedRemoteId(id, remoteId)
    }

    @WorkerThread
    suspend fun deleteMixerData(mixer: Mixer){
        mixerDao.deleteMixer(mixer)
    }
}