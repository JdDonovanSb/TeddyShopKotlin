package Models.Usuarios

import com.google.gson.annotations.SerializedName

data class Roles(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("estado") var estado: Boolean,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("usuarios") var usuarios: List<String>? = emptyList()
)