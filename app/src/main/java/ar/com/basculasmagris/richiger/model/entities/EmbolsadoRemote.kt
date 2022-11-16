package ar.com.basculasmagris.richiger.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class EmbolsadoRemote (
    val id: Long,
    val cliente_id: Long,
    val equipo_picado_id: Long,
    var operario_id: Long,
    var embolsadora_id: Long,
    var material_id: Long,
    var bolsa_id: Long,
    var localidad_id: Long,
    val obs: String,
    val gps: String,
    val fecha_inicio: Date,
    val fecha_fin: Date,
    val metros_embolsados: Float,
    val kg_embolsados: Float,
    val humedad: Float,
    var remoteId : Long
) : Parcelable