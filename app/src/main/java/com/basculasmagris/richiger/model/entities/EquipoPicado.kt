package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(
    tableName = "equipoPicado",
    primaryKeys = ["id","product_id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Direccion::class,
            childColumns = ["direccionId"],
            parentColumns = ["id"]
        )
    ]
)
data class EquipoPicado (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val direccionIn: Long,
    @ColumnInfo val cuit: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable