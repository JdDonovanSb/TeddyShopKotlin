package Models.Productos

import com.google.gson.annotations.SerializedName

class Producto (
    @SerializedName("_id") val id: String? = null,
    @SerializedName("estiloProducto") var estiloProducto: String,
    @SerializedName("materialProducto") var materialProducto: String,
    @SerializedName("disponibilidadProducto") var disponibilidadProducto: String,
    @SerializedName("tamañoProducto") var tamañoProducto: String,
    @SerializedName("imagen") var imagen: String? = null,
    @SerializedName("historialPrecios") var historialPrecios: List<String>? = emptyList(),
    //@SerializedName("catalogos") var catalogos: List<String>? = emptyList(),
    //@SerializedName("categorias") var categorias: List<String>? = emptyList(),
)
