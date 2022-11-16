package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClienteRemote (
    val name: String,
    val titular: String,
    val email: String,
    val cuit: String,
    val direccionId: Long,
    val id: Long,
    val localidadId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable