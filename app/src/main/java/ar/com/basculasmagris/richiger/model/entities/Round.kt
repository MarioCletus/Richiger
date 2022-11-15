package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Diet::class,
            childColumns = ["diet_id"],
            parentColumns = ["id"]
        )
    ]
)
data class Round (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @ColumnInfo(name = "weight") val weight: Double,
    @ColumnInfo(name = "use_percentage") val usePercentage: Boolean,
    @ColumnInfo(name = "custom_percentage") val customPercentage: Double,
    @ColumnInfo(name = "diet_id") val dietId: Long,
    @ColumnInfo(name = "remote_diet_id") val remoteDietId: Long,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable