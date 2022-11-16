package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "operario",
    primaryKeys = ["id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Direccion::class,
            childColumns = ["direccionId"],
            parentColumns = ["id"]
        )
    ]
)
data class Operario (
    @ColumnInfo val name: String,
    @ColumnInfo val email: String,
    @ColumnInfo val cuit: String,
    @ColumnInfo val direccionId: Long,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable