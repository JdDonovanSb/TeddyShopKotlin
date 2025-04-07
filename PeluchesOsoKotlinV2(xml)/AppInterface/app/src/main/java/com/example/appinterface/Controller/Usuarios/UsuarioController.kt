// UsuarioController.kt
package Controller.Usuarios

import Models.Usuarios.Usuario
import com.example.appinterface.Api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioController {

    private val api = RetrofitClient.usuarioService

    fun listarUsuarios(callback: (List<Usuario>?) -> Unit) {
        api.listarUsuario().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    println("Error en listarUsuarios: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                println("Error de red en listarUsuarios: ${t.message}")
                callback(null)
            }
        })
    }

    fun crearUsuario(email: String, contrasena: String, username: String, callback: (Boolean) -> Unit) {
        val usuario = Usuario(email = email, contrasena = contrasena, username = username)

        println("Intentando crear usuario: $usuario")

        api.crearUsuario(usuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    val error = response.errorBody()?.string()
                    println("Error al crear usuario (response): $error")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red al crear usuario: ${t.message}")
                callback(false)
            }
        })
    }


    fun obtenerUsuarioPorId(id: String, callback: (Usuario?) -> Unit) {
        api.obtenerUsuarioPorId(id).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                callback(response.body())
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println("Error de red en obtenerUsuarioPorId: ${t.message}")
                callback(null)
            }
        })
    }

    fun actualizarUsuario(id: String, email: String, contrasena: String, username: String, callback: (Boolean) -> Unit) {
        val usuario = Usuario(email = email, contrasena = contrasena, username = username)

        api.actualizarUsuario(id, usuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en actualizarUsuario: ${t.message}")
                callback(false)
            }
        })
    }

    fun eliminarUsuario(id: String, callback: (Boolean) -> Unit) {
        api.eliminarUsuario(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error de red en eliminarUsuario: ${t.message}")
                callback(false)
            }
        })
    }
}