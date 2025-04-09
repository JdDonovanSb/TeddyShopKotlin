package Controller.Usuarios

import Models.Usuarios.Clientes
import Network.ClientesApiService
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientesController {

    private val api: ClientesApiService = RetrofitClient.clientesService

    fun listarClientes(callback: (List<Clientes>?) -> Unit) {
        api.listarClientes().enqueue(object : Callback<List<Clientes>> {
            override fun onResponse(call: Call<List<Clientes>>, response: Response<List<Clientes>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    logError("listarClientes", response)
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Clientes>>, t: Throwable) {
                logNetworkError("listarClientes", t)
                callback(null)
            }
        })
    }

    fun crearCliente(dni: String, nombre: String, telefono: String, apellido: String, callback: (Boolean) -> Unit) {
        val cliente = parseCliente(null, dni, nombre, telefono, apellido) ?: run {
            println("Formato inválido de DNI o Teléfono en crearCliente.")
            callback(false)
            return
        }

        api.crearCliente(cliente).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logNetworkError("crearCliente", t)
                callback(false)
            }
        })
    }

    fun actualizarCliente(dni: String, nombre: String, telefono: String, apellido: String, callback: (Boolean) -> Unit) {
        buscarClientePorDni(dni) { clienteExistente ->
            if (clienteExistente == null || clienteExistente.id.isNullOrEmpty()) {
                callback(false)
                return@buscarClientePorDni
            }

            val clienteActualizado = try {
                Clientes( // Usar el modelo Clientes, pero con id = null
                    id = null, // ⬅️ Clave para excluir _id del JSON
                    dniCliente = dni.toInt(),
                    nombreCliente = nombre,
                    apellidoCliente = apellido,
                    telefonoCliente = telefono
                )
            } catch (e: NumberFormatException) {
                callback(false)
                return@buscarClientePorDni
            }

            // Usar el ID del clienteExistente en la URL, no en el cuerpo
            api.actualizarCliente(clienteExistente.id, clienteActualizado).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    callback(response.isSuccessful)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    logNetworkError("actualizarCliente", t)
                    callback(false)
                }
            })
        }
    }


    fun eliminarCliente(dni: String, callback: (Boolean) -> Unit) {
        buscarClientePorDni(dni) { cliente ->
            if (cliente == null || cliente.id.isNullOrEmpty()) {
                println("Cliente no encontrado o sin ID válido.")
                callback(false)
                return@buscarClientePorDni
            }

            api.eliminarCliente(cliente.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    callback(response.isSuccessful)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    logNetworkError("eliminarCliente", t)
                    callback(false)
                }
            })
        }
    }

    fun buscarClientePorDni(dni: String, callback: (Clientes?) -> Unit) {
        val dniInt = dni.toIntOrNull()
        if (dniInt == null) {
            println("DNI inválido en buscarClientePorDni.")
            callback(null)
            return
        }

        listarClientes { clientes ->
            val cliente = clientes?.find { it.dniCliente == dniInt }
            callback(cliente)
        }
    }

    private fun parseCliente(id: String?, dni: String, nombre: String, telefono: String, apellido: String): Clientes? {
        return try {
            Clientes(
                id = id,
                dniCliente = dni.toInt(),
                nombreCliente = nombre,
                apellidoCliente = apellido,
                telefonoCliente = telefono
            )
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun logNetworkError(funcion: String, t: Throwable) {
        println("Error de red en $funcion: ${t.message}")
    }

    private fun logError(funcion: String, response: Response<*>) {
        println("Error en $funcion: ${response.errorBody()?.string()}")
    }
}