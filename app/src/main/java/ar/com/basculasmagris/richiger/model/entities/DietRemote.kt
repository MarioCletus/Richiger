package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class DietRemote (
    val name: String,
    val description: String,
    val id: Long,
    val appId: Long,
    val updatedDate: String,
    val archiveDate: String,
    val usePercentage: Boolean,
    val products: ArrayList<DietProductDetail>
) : Parcelable

