package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DireccionRemote (
    val calle: String,
    val numero: String,
    val id: Long,
    val localidadId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable