package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class DescargaRemote (
    val id: Long,
    val operario_id: Long,
    val carro_id: Long,
    val embolsado_id: Long,
    val kg_descarga: Int,
    val metros_embolsados: Float,
    var remoteId: Long
)  : Parcelable