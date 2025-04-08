package Models.Usuarios

import com.google.gson.annotations.SerializedName

data class Empleados(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("dniEmpleado") var dniEmpleado: Long,
    @SerializedName("telefonoEmpleado") var telefonoEmpleado: Long,
    @SerializedName("nombreEmpleado") var nombreEmpleado: String
    //@SerializedName("companias") var companias: List<String>? = emptyList()
)
