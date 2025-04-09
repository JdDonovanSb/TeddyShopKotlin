package Models.Pedidos

import com.google.gson.annotations.SerializedName

data class DetallePedido(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("numDetalle") val numDetallePedido: Int?,
    @SerializedName("precioDetallePedido") val precioDetallePedido: Double,
    @SerializedName("cantidadDetallePedido") val cantidadDetallePedido: Int
)
