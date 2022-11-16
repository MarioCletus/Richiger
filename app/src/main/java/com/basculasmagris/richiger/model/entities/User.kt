package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User (
    @PrimaryKey val username: String,
    @ColumnInfo val name: String,
    @ColumnInfo val lastname: String,
    @ColumnInfo(name = "code_role") val codeRole: Int
) : Parcelable