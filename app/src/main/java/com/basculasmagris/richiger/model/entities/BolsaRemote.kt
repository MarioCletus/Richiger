package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BolsaRemote (
    val name: String,
    val marca: String,
    val tamanio: Double,
    val volumen: Double,
    val largo: Long,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable