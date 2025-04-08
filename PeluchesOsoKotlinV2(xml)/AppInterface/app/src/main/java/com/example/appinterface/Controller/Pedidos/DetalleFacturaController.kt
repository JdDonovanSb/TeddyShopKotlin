package Controller.Pedidos

import Models.Pedidos.DetalleFactura
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleFacturaController {

    private val api = RetrofitClient.detalleFacturaService

    fun listarDetallesFactura(callback: (List<DetalleFactura>?) -> Unit) {
        api.listarDetallesFactura().enqueue(object : Callback<List<DetalleFactura>> {
            override fun onResponse(call: Call<List<DetalleFactura>>, response: Response<List<DetalleFactura>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error en listarDetallesFactura: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<DetalleFactura>>, t: Throwable) {
                println("Error de red en listarDetallesFactura: ${t.message}")
                callback(null)
            }
        })
    }

    fun crearDetalleFactura(numDetalle: Int, precio: Double, cantidad: Int, callback: (Boolean) -> Unit) {
        val detalle = DetalleFactura(
            numDetalle = numDetalle,
            precioDetalleFactura = precio,
            cantidadDetalleFactura = cantidad
        )

        api.crearDetalleFactura(detalle).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en crearDetalleFactura: ${t.message}")
                callback(false)
            }
        })
    }


    fun actualizarDetalleFactura(id: String, numDetalle: Int, precio: Double, cantidad: Int, callback: (Boolean) -> Unit) {
        val detalle = DetalleFactura(
            numDetalle = numDetalle,
            precioDetalleFactura = precio,
            cantidadDetalleFactura = cantidad
        )

        api.actualizarDetalleFactura(id, detalle).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en actualizarDetalleFactura: ${t.message}")
                callback(false)
            }
        })
    }

    fun eliminarDetalleFactura(id: String, callback: (Boolean) -> Unit) {
        api.eliminarDetalleFactura(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en eliminarDetalleFactura: ${t.message}")
                callback(false)
            }
        })
    }
}
