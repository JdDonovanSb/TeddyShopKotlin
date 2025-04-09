package Models.Pedidos

import android.icu.text.DateFormat
import com.google.gson.annotations.SerializedName

data class Devoluciones(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("detalleDevolucion") var detalleDevolucion: String,
    //@SerializedName("inventario") var inventarios: List<String>? = emptyList(),
    )