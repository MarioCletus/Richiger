package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarroRemote (
    val name: String,
    val marca: String,
    val modelo: String,
    val volumen: Double,
    val balanza: Boolean,
    var remoteId: Long,
    val archiveDate: String?,
    var appId: Long = 0
) : Parcelable