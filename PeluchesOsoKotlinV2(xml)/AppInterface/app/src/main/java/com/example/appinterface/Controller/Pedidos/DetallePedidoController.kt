package Controller.Pedidos

import Models.Pedidos.DetallePedido
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class DetallePedidoController {

    private val api = RetrofitClient.detallePedidoService

    fun crearDetallePedido(numDetalle: Int, precio: Double, cantidad: Int, callback: (Boolean) -> Unit) {
        val detallePedido = DetallePedido(numDetallePedido = numDetalle, precioDetallePedido = precio, cantidadDetallePedido = cantidad)

        Log.d("DetalleController", "Llamando a la API para crear detalle: $detallePedido")

        api.crearDetallePedido(detallePedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("CREAR", "Código: ${response.code()}, éxito=${response.isSuccessful}")
                Log.d("CREAR", "ErrorBody: ${response.errorBody()?.string()}")
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("DetalleCont3roller", "Fallo en la petición: ${t.message}", t)
                callback(false)
            }
        })
    }

    fun actualizarDetallePedido(id: String, numDetallePedido: Int?, precio: Double, cantidad: Int, callback: (Boolean) -> Unit) {
        val detallePedido = DetallePedido(
            numDetallePedido = numDetallePedido, // Puedes ajustar esto si es necesario enviarlo
            precioDetallePedido = precio,
            cantidadDetallePedido = cantidad
        )

        api.actualizarDetallePedido(id, detallePedido).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Log.e("ACTUALIZAR_ERROR", "Código: ${response.code()} - Error: ${response.errorBody()?.string()}")
                }
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ACTUALIZAR_FAILURE", "Error de red: ${t.message}")
                callback(false)
            }
        })
    }

    fun listarDetallesPedido(callback: (List<DetallePedido>?) -> Unit) {
        api.listarDetallesPedido().enqueue(object : Callback<List<DetallePedido>> {
            override fun onResponse(
                call: Call<List<DetallePedido>>,
                response: Response<List<DetallePedido>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<DetallePedido>>, t: Throwable) {
                callback(null)
            }
        })
    }


    fun eliminarDetallePedido(id: String, callback: (Boolean) -> Unit) {
        api.eliminarDetallePedido(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
