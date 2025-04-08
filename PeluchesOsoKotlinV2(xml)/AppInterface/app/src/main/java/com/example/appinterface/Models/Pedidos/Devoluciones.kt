package Models.Pedidos

import android.icu.text.DateFormat
import com.google.gson.annotations.SerializedName

data class Devoluciones(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("numDevolucion") var numDevolucion: String,
    @SerializedName("detalleDevolucion") var detalleDevolucion: String,
    @SerializedName("fechaDevolucion") var fechaDevolucion: String,
    @SerializedName("productos") var productos: List<String>? = emptyList(),
    )