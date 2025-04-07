package Models.Productos

import com.google.gson.annotations.SerializedName

data class Catalogo(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("nombreCatalogo") var nombreCatalogo: String,
    @SerializedName("descripcionCatalogo") var descripcionCatalogo: String,
    @SerializedName("disponibilidadCatalogo") var disponibilidadCatalogo: Boolean,
    @SerializedName("estiloCatalogo") var estiloCatalogo: String,
    @SerializedName("imagen") var imagen: String? = null,
    @SerializedName("productos") var productos: List<String>? = emptyList(),
    //@SerializedName("companias") var companias: List<String>? = emptyList()
)