package Models.Productos

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("_id") val id: String? = null, // Puede ser nulo si creamos una nueva categoría
    @SerializedName("nombreCategoria") var nombre: String,
    @SerializedName("descripcionCategoria") var descripcion: String,
    @SerializedName("imagen") var imagen: String? = null, // La API devuelve una imagen, puede ser opcional
    @SerializedName("productos") var productos: List<String>? = emptyList() // Lista de productos opcional
)
