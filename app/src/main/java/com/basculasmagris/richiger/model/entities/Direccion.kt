package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "direccion",
    primaryKeys = ["id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Localidad::class,
            childColumns = ["localidadId"],
            parentColumns = ["id"]
        )
    ]
)
data class Direccion (
    @ColumnInfo val calle: String,
    @ColumnInfo val numero: String,
    @ColumnInfo val localidadId: Long,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable