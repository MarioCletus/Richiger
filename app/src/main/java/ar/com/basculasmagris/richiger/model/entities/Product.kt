package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "product")
data class Product (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "specific_weight") val specificWeight: Double,
    @ColumnInfo val rfid: Long,
    @ColumnInfo val image: String,
    @ColumnInfo val imageSource: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable