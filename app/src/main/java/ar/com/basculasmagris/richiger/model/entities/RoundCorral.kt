package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "round_corral",
    primaryKeys = ["round_id","corral_id"],
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = Corral::class,
                childColumns = ["corral_id"],
                parentColumns = ["id"]
            ),
            androidx.room.ForeignKey(
                entity = Round::class,
                childColumns = ["round_id"],
                parentColumns = ["id"]
            )
    ]
)
data class RoundCorral (
    @ColumnInfo(name = "round_id") val roundId: Long,
    @ColumnInfo(name = "corral_id") val corralId: Long,
    @ColumnInfo(name = "remote_round_id") var remoteRoundId: Long,
    @ColumnInfo(name = "remote_corral_id") var remoteCorralId: Long,
    @ColumnInfo val order: Int,
    @ColumnInfo val weight: Double,
    @ColumnInfo val percentage: Double,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
)  : Parcelable