package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DietProductDetail (
    val productId: Long,
    val remoteProductId: Long,
    val productName: String,
    val productDescription: String,
    val dietId: Long,
    val remoteDietId: Long,
    var percentage: Double,
    var weight: Double,
    var order: Int,
) : Parcelable