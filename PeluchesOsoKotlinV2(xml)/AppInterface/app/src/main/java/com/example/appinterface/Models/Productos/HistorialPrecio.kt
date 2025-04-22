package Models.Productos

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HistorialPrecio(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("precio") var precio: Double,
    @SerializedName("fechaInicio") var fechaInicio: Date,
    @SerializedName("fechaFin") var fechaFin: Date,
    @SerializedName("estadoPrecio") var estadoPrecio: Boolean,
    //@SerializedName("producto") var producto: List<String>? = emptyList(),
)
