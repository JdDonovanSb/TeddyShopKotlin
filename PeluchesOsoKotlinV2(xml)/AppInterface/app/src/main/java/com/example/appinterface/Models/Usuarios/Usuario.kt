package Models.Usuarios

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("email") var email: String,
    @SerializedName("contraseña") var contrasena: String,
    @SerializedName("username") var username: String,
)
