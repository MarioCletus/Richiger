package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bolsa")
data class Bolsa (
    @ColumnInfo val name: String,
    @ColumnInfo val marca: String,
    @ColumnInfo val tamanio: Double,
    @ColumnInfo val volumen: Double,
    @ColumnInfo val largo: Long,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable