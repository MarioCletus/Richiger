package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "diet")
data class Diet (
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @ColumnInfo(name = "user_percentage") val usePercentage: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
)  : Parcelable