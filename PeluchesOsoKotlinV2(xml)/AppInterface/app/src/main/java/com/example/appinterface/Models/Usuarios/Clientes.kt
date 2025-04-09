package Models.Usuarios

import com.google.gson.annotations.SerializedName

data class Clientes(
    @SerializedName("_id") val id: String?, // <--- Esto es clave para actualizar/eliminar
    @SerializedName("dniCliente") val dniCliente: Int,
    @SerializedName("nombreCliente") val nombreCliente: String,
    @SerializedName("apellidoCliente") val apellidoCliente: String,
    @SerializedName("telefonoCliente") val telefonoCliente: String
)
