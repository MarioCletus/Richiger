package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class RoundRemote (
    val name: String,
    val description: String,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String,
    //var percentage: Double,
    var weight: Double,
    var usePercentage: Boolean,
    var customPercentage: Double,
    var dietId: Long,
    //var order: Int,
    val corrals: ArrayList<RoundCorralDetail>
) : Parcelable

