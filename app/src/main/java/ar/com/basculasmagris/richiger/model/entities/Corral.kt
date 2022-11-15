package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "corral",
    foreignKeys = [
        ForeignKey(
            entity = Establishment::class,
            childColumns = ["establishment_id"],
            parentColumns = ["id"])
    ],
    indices = [
        Index("establishment_id")
    ]
)
data class Corral (
    @ColumnInfo(name = "establishment_id") val establishmentId: Long,
    @ColumnInfo(name = "establishment_remote_id") var establishmentRemoteId: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @ColumnInfo(name = "animal_quantity") val animalQuantity: Int,
    @ColumnInfo val rfid: Long,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable