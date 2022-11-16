package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mixer")
data class Mixer(
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val mac: String,
    @ColumnInfo(name = "bt_box") val btBox: String,
    @ColumnInfo val tara: Int,
    @ColumnInfo val calibration: Float,
    @ColumnInfo val rfid: Long,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") var updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable