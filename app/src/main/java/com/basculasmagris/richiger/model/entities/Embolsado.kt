package com.basculasmagris.richiger.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(
    tableName = "embolsado",
    primaryKeys = ["id"],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Cliente::class,
            childColumns = ["cliente_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = EquipoPicado::class,
            childColumns = ["equipo_picado_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Operario::class,
            childColumns = ["operario_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Embolsadora::class,
            childColumns = ["embolsadora_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Material::class,
            childColumns = ["material_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Bolsa::class,
            childColumns = ["bolsa_id"],
            parentColumns = ["id"]
        ),
        androidx.room.ForeignKey(
            entity = Localidad::class,
            childColumns = ["localidad_id"],
            parentColumns = ["id"]
        )
    ]
)
data class Embolsado (
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "cliente_id") val cliente_id: Long,
    @ColumnInfo(name = "equipo_picado_id") val equipo_picado_id: Long,
    @ColumnInfo(name = "operario_id") var operario_id: Long,
    @ColumnInfo(name = "embolsadora_id") var embolsadora_id: Long,
    @ColumnInfo(name = "material_id") var material_id: Long,
    @ColumnInfo(name = "bolsa_id") var bolsa_id: Long,
    @ColumnInfo(name = "localidad_id") var localidad_id: Long,
    @ColumnInfo val obs: String,
    @ColumnInfo val gps: String,
    @ColumnInfo val fecha_inicio: Date,
    @ColumnInfo val fecha_fin: Date,
    @ColumnInfo val metros_embolsados: Float,
    @ColumnInfo val kg_embolsados: Float,
    @ColumnInfo val humedad: Float,
    @ColumnInfo(name = "remote_id") var remoteId: Long,
    @ColumnInfo(name = "updated_date") val updatedDate: String,
    @ColumnInfo(name = "archive_date") val archiveDate: String?,
)  : Parcelable