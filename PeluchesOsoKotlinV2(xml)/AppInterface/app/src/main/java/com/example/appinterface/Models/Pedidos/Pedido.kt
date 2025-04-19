package Models.Pedidos

import com.google.gson.annotations.SerializedName

data class Pedido (
   @SerializedName("_id") val id: String? = null,
   @SerializedName("nombreComprador") var nombreComprador: String,
   @SerializedName("numeroComprador") var numeroComprador: String,
   @SerializedName("nombreAgendador") var nombreAgendador: String,
   @SerializedName("numeroAgendador") var numeroAgendador: String,
   @SerializedName("localidad") var localidad: String,
   @SerializedName("direccion") var direccion: String,
   @SerializedName("barrio") var barrio: String,
   //@SerializedName("cliente") var cliente: List<String>? = emptyList(),
   @SerializedName("detallesPedido") var detallesPedido: List<String>? = emptyList(),
   //@SerializedName("facturas") var facturas: List<String>? = emptyList(),
   )
