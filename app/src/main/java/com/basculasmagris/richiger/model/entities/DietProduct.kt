package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "diet_product",
    primaryKeys = ["diet_id","product_id"],
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = Product::class,
                childColumns = ["product_id"],
                parentColumns = ["id"]
            ),
            androidx.room.ForeignKey(
                entity = Diet::class,
                childColumns = ["diet_id"],
                parentColumns = ["id"]
            )
    ]
)
data class DietProduct (
    @ColumnInfo(name = "diet_id") val dietId: Long,
    @ColumnInfo(name = "product_id") val productId: Long,
    @ColumnInfo(name = "remote_diet_id") var remoteDietId: Long,
    @ColumnInfo(name = "remote_product_id") var remoteProductId: Long,
    @ColumnInfo val order: Int,
    @ColumnInfo val weight: Double,
    @ColumnInfo val percentage: Double,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
)  : Parcelable