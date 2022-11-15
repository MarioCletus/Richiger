package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MixerRemote (
    val name: String,
    val description: String,
    val mac: String,
    val btBox: String,
    val tara: Int,
    val calibration: Float,
    val rfid: Long,
    val remoteId: Long,
    val updatedDate: String,
    val id: Long = 0,
    val appId: Long,
    val archiveDate: String
) : Parcelable