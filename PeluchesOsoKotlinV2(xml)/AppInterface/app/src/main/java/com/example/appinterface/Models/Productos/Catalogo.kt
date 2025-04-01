package Models.Productos

import com.google.gson.annotations.SerializedName

data class Catalogo(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("nombreCatalogo") var nombre: String,
    @SerializedName("descripcionCatalogo") var descripcion: String,
    @SerializedName("disponibilidadCatalogo") var disponible: Boolean,
    @SerializedName("estiloCatalogo") var estilo: String,
    @SerializedName("productos") var productos: List<String>? = emptyList() // Lista de productos opcional
)