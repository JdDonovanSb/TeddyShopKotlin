package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.UsuarioController
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString
import android.widget.Toast

class UsuarioActivity : AppCompatActivity() {

    private val usuarioController = UsuarioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)
    }

    // Función que muestra el formulario de crear usuario
    fun mostrarFormulario(v: View) {
        val formulario = findViewById<LinearLayout>(R.id.contenedorFormulario)
        if (formulario.visibility == View.GONE) {
            formulario.visibility = View.VISIBLE
        } else {
            formulario.visibility = View.GONE
        }
    }
    fun mostrarFormularioBuscar(v: View) {
        val formulario = findViewById<LinearLayout>(R.id.contenedorFormularioBuscar)
        if (formulario.visibility == View.GONE) {
            formulario.visibility = View.VISIBLE
        } else {
            formulario.visibility = View.GONE
        }
    }
    fun mostrarFormularioActualizar(v: View) {
        val formularioActualizar = findViewById<LinearLayout>(R.id.contenedorFormularioActualizar)
        formularioActualizar.visibility = if (formularioActualizar.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    fun mostrarFormularioEliminar(v: View) {
        val formularioEliminar = findViewById<LinearLayout>(R.id.contenedorFormularioEliminar)
        formularioEliminar.visibility = if (formularioEliminar.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    fun crearUsuario(v: View) {
        val email = findViewById<EditText>(R.id.emailUsuario).text.toString().trim()
        val contrasena = findViewById<EditText>(R.id.contrasenaUsuario).text.toString().trim()
        val username = findViewById<EditText>(R.id.usernameUsuario).text.toString().trim()

        if (email.isEmpty() || contrasena.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioController.crearUsuario(email, contrasena, username) { success ->
            runOnUiThread {
                Toast.makeText(this, if (success) "Usuario creado con éxito" else "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun listarUsuarios(v: View) {
        usuarioController.listarUsuarios { usuarios ->
            runOnUiThread {
                val textView = findViewById<TextView>(R.id.textViewUsuarios)
                if (usuarios != null && usuarios.isNotEmpty()) {
                    textView.text = usuarios.joinToString("\n") {
                        "Email: ${it.email}\nContraseña: ${it.contrasena}\nUsername: ${it.username}\n"
                    }
                } else {
                    textView.text = "No hay usuarios disponibles"
                }
            }
        }
    }

    fun buscarUsuario(v: View) {
        val id = findViewById<EditText>(R.id.idUsuario).text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "ID no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioController.obtenerUsuarioPorId(id) { usuario ->
            runOnUiThread {
                val textView = findViewById<TextView>(R.id.textViewUsuarios)
                if (usuario != null) {
                    textView.text = "Email: ${usuario.email}\nContraseña: ${usuario.contrasena}\nUsername: ${usuario.username}"
                } else {
                    textView.text = "Usuario no encontrado"
                }
            }
        }
    }

    fun actualizarUsuario(v: View) {
        val id = findViewById<EditText>(R.id.idUsuarioActualizar).text.toString().trim()
        val email = findViewById<EditText>(R.id.emailUsuarioActualizar).text.toString().trim()
        val contrasena = findViewById<EditText>(R.id.contrasenaUsuarioActualizar).text.toString().trim()
        val username = findViewById<EditText>(R.id.usernameUsuarioActualizar).text.toString().trim()


        if (id.isEmpty() || email.isEmpty() || contrasena.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioController.actualizarUsuario(id, email, contrasena, username) { success ->
            runOnUiThread {
                Toast.makeText(this, if (success) "Usuario actualizado correctamente" else "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun eliminarUsuario(v: View) {
        val id = findViewById<EditText>(R.id.idUsuarioEliminar).text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un ID", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioController.eliminarUsuario(id) { success ->
            runOnUiThread {
                Toast.makeText(this, if (success) "Usuario eliminado correctamente" else "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
