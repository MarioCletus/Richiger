package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CorralRemote (
    val id: Long,
    val establishmentId: Long,
    val establishmentRemoteId: Long,
    val name: String,
    val description: String,
    val remoteId: Long,
    val updatedDate: String,
    val animalQuantity: Int,
    val rfid: Long,
    val appId: Long,
    val archiveDate: String
) : Parcelable