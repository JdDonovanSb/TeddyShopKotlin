package Models

import com.google.gson.annotations.SerializedName

class Compania (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("NIT") var NIT: Int? = null,
    @SerializedName("telefonoEmpresa") var telefonoEmpresa: String,
    @SerializedName("nombreEmpresa") var nombreEmpresa: String,
    @SerializedName("direccionEmpresa") var direccionEmpresa: String,
    @SerializedName("catalogos") var catalogos: List<String>? = emptyList(),
    @SerializedName("empleados") var empleados: List<String>? = emptyList(),
)
