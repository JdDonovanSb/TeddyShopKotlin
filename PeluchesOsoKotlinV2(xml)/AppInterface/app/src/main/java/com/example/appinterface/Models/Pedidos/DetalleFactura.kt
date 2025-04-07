package Models.Pedidos

import com.google.gson.annotations.SerializedName

data class DetalleFactura(
    @SerializedName("numDetalle") var numDetalle: Int,
    @SerializedName("precioDetalleFactura") var precioDetalleFactura: Double,
    @SerializedName("cantidadDetalleFactura") var cantidadDetalleFactura: Int
)
