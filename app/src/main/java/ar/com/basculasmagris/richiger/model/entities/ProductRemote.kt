package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductRemote (
    val name: String,
    val description: String,
    val specificWeight: Double,
    val rfid: Long,
    val image: String,
    val imageSource: String,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable