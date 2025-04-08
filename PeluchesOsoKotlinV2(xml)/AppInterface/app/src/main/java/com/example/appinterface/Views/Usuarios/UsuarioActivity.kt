package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.UsuarioController
import Models.Usuarios.Usuario
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class UsuarioActivity : AppCompatActivity() {
    private val usuarioController = UsuarioController()
    private var usuarioSeleccionado: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearUsuario(v: View) {
        val email = findViewById<EditText>(R.id.emailUsuario).text.toString().trim()
        val contrasena = findViewById<EditText>(R.id.contrasenaUsuario).text.toString().trim()
        val username = findViewById<EditText>(R.id.usernameUsuario).text.toString().trim()

        if (email.isEmpty() || contrasena.isEmpty() || username.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        usuarioController.crearUsuario(email, contrasena, username) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Usuario creado con éxito")
                    limpiarFormulario()
                } else {
                    mostrarToast("Error al crear usuario")
                }
            }
        }
    }

    fun listarUsuarios(v: View) {
        usuarioController.listarUsuarios { usuarios ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableUsuarios)
                table.removeViews(1, table.childCount - 1)

                usuarios?.forEach { usuario ->
                    val row = TableRow(this).apply {
                        addView(createCell(usuario.username))
                        addView(createCell(usuario.email))
                        addView(crearBotonesAccion(usuario))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar usuarios")
            }
        }
    }

    private fun crearBotonesAccion(usuario: Usuario): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(usuario) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(usuario) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(usuario) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(usuario: Usuario) {
        usuarioSeleccionado = usuario
        findViewById<ConstraintLayout>(R.id.layoutActualizarUsuario).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextEmail).setText(usuario.email)
        findViewById<EditText>(R.id.editTextUsername).setText(usuario.username)
        findViewById<EditText>(R.id.editTextContrasena).setText(usuario.contrasena)
    }

    fun actualizarUsuario(v: View) {
        usuarioSeleccionado?.let { usuario ->
            val nuevoEmail = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val nuevoUsername = findViewById<EditText>(R.id.editTextUsername).text.toString()
            val nuevaContrasena = findViewById<EditText>(R.id.editTextContrasena).text.toString()

            usuarioController.actualizarUsuario(
                usuario.id ?: return,
                nuevoEmail,
                nuevaContrasena,
                nuevoUsername
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<ConstraintLayout>(R.id.layoutActualizarUsuario).visibility = View.GONE
                        listarUsuarios(findViewById(R.id.listarUsuarios))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(usuario: Usuario) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar usuario")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                usuario.id?.let { id ->
                    usuarioController.eliminarUsuario(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminado")
                                listarUsuarios(findViewById(R.id.listarUsuarios))
                            } else {
                                mostrarToast("Error al eliminar")
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(usuario: Usuario) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Usuario")
            .setMessage("""
                Username: ${usuario.username}
                Email: ${usuario.email}
                Contraseña: ${usuario.contrasena}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.emailUsuario).text.clear()
        findViewById<EditText>(R.id.contrasenaUsuario).text.clear()
        findViewById<EditText>(R.id.usernameUsuario).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}