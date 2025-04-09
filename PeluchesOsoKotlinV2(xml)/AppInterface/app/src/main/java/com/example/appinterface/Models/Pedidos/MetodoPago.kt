package Models.Pedidos

import com.google.gson.annotations.SerializedName

data class MetodoPago(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("nombreMetodoPago") var nombreMetodoPago: String

)
