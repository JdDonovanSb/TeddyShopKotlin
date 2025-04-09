package Models.Pedidos

import com.google.gson.annotations.SerializedName

data class Factura(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("fechaCreacionFactura") var fechaCreacionFactura: String,
    @SerializedName("horaCreacionFactura") var horaCreacionFactura: String,
)
