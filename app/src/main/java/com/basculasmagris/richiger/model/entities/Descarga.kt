package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "descarga",
    primaryKeys = ["id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Operario::class,
            childColumns = ["operario_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Carro::class,
            childColumns = ["carro_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Embolsado::class,
            childColumns = ["embolsado_id"],
            parentColumns = ["id"]
        )
    ]
)
data class Descarga (
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "operario_id") val operario_id: Long,
    @ColumnInfo(name = "carro_id") val carro_id: Long,
    @ColumnInfo(name = "embolsado_id") val embolsado_id: Long,
    @ColumnInfo val kg_descarga: Int,
    @ColumnInfo val metros_embolsados: Float,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
)  : Parcelable