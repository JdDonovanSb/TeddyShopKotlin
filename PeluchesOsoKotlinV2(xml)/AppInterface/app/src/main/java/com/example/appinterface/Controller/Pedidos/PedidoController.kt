package Controller.Pedidos

import Models.Pedidos.Pedido
import com.example.appinterface.Api.Pedidos.PedidoApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response
import android.util.Log


class PedidoController {
    private val api = RetrofitClient.pedidoService

    // Crear un pedido
    fun crearPedido(
        nombreComprador: String,
        numeroComprador: String,
        nombreAgendador: String,
        numeroAgendador: String,
        localidad: String,
        direccion: String,
        barrio: String,
        //cliente: List<String>?,
        detallesPedido: List<String>?,
        //facturas: List<String>?,
        callback: (Boolean) -> Unit
    ) {
        Log.d("PedidoController", "Iniciando la creación de pedido...")

        val pedido = Pedido(
            id = null,
            nombreComprador = nombreComprador,
            numeroComprador = numeroComprador,
            nombreAgendador = nombreAgendador,
            numeroAgendador = numeroAgendador,
            localidad = localidad,
            direccion = direccion,
            barrio = barrio,
            //cliente = cliente ?: emptyList(),
            detallesPedido = detallesPedido ?: emptyList(),
            //facturas = facturas ?: emptyList()
        )

        api.crearPedido(pedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.d("PedidoController", "Pedido creado exitosamente.")
                    callback(true)
                } else {
                    Log.e("PedidoController", "Error al crear pedido: ${response.errorBody()?.string()}")
                    callback(false)
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PedidoController", "Fallo al crear pedido: ${t.message}")
                callback(false)
            }
        })
    }


    // Listar todos los pedidos
    fun listarPedidos(callback: (List<Pedido>?) -> Unit) {
        Log.d("PedidoController", "Iniciando la solicitud para listar pedidos...")

        api.listarPedidos().enqueue(object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                if (response.isSuccessful) {
                    Log.d("PedidoController", "Pedidos obtenidos: ${response.body()}")
                    callback(response.body())
                } else {
                    Log.e("PedidoController", "Error al obtener pedidos: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                Log.e("PedidoController", "Fallo la solicitud para listar pedidos: ${t.message}")
                callback(null)
            }
        })
    }


    // Actualizar pedido por ID
    fun actualizarPedido(
        id: String, nombreComprador: String, numeroComprador: String,
        nombreAgendador: String, numeroAgendador: String, localidad: String,
        direccion: String, barrio: String, callback: (Boolean) -> Unit
    ) {
        Log.d("PedidoController", "Iniciando la actualización del pedido con ID: $id")

        val pedido = Pedido(
            nombreComprador = nombreComprador, numeroComprador = numeroComprador, nombreAgendador = nombreAgendador,
            numeroAgendador = numeroAgendador, localidad = localidad, direccion = direccion, barrio = barrio
        )

        api.actualizarPedido(id, pedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("PedidoController", "Pedido actualizado exitosamente.")
                } else {
                    Log.e("PedidoController", "Error al actualizar pedido: ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PedidoController", "Fallo al actualizar pedido: ${t.message}")
                callback(false)
            }
        })
    }



    // Eliminar pedido por ID
    fun eliminarPedido(id: String, callback: (Boolean) -> Unit) {
        Log.d("PedidoController", "Iniciando la eliminación del pedido con ID: $id")

        api.eliminarPedido(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("PedidoController", "Pedido eliminado exitosamente.")
                } else {
                    Log.e("PedidoController", "Error al eliminar pedido: ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("PedidoController", "Fallo al eliminar pedido: ${t.message}")
                callback(false)
            }
        })
    }
}
