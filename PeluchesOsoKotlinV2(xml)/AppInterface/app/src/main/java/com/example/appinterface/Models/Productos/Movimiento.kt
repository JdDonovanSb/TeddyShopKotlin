package Models.Productos

import com.google.gson.annotations.SerializedName

data class Movimiento(
    @SerializedName("_id") val id: String?,
    @SerializedName("fecha") var fecha: String,
    @SerializedName("cantidadIngreso") var cantidadIngreso: Int,
    @SerializedName("cantidadVendida") var cantidadVendida: Int,
    @SerializedName("inventario") var inventario: String? = null // ← hacerlo nullable
)

