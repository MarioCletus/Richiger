package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialRemote (
    val name: String,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String
) : Parcelable