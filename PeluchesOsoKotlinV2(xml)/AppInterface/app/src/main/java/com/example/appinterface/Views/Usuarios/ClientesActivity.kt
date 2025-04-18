package com.example.appinterface.Views.Usuarios

import Controller.Usuarios.ClientesController
import Models.Usuarios.Clientes
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R


class ClientesActivity : AppCompatActivity() {

    private val clientesController = ClientesController()
    private var clienteSeleccionado: Clientes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearCliente(v: View) {
        val dni = findViewById<EditText>(R.id.editTextDni).text.toString().trim()
        val nombre = findViewById<EditText>(R.id.editTextNombre).text.toString().trim()
        val apellido = findViewById<EditText>(R.id.editTextApellido).text.toString().trim()
        val telefono = findViewById<EditText>(R.id.editTextTelefono).text.toString().trim()

        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        clientesController.crearCliente(dni, nombre, telefono, apellido) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Cliente creado con éxito")
                    limpiarFormulario()
                    listarClientes(v)
                } else {
                    mostrarToast("Error al crear cliente")
                }
            }
        }
    }

    fun listarClientes(v: View) {
        clientesController.listarClientes { lista ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableClientes)
                table.removeViews(1, table.childCount - 1)

                lista?.forEach { cliente ->
                    val row = TableRow(this).apply {
                        addView(createCell(cliente.nombreCliente))
                        addView(createCell(cliente.apellidoCliente))
                        addView(createCell(cliente.telefonoCliente))
                        addView(crearBotonesAccion(cliente))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar clientes")
            }
        }
    }

    private fun crearBotonesAccion(cliente: Clientes): LinearLayout {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER
        }

        val btnEditar = ImageButton(this).apply {
            setImageResource(R.drawable.ic_baseline_edit_24)
            background = null
            setOnClickListener { mostrarFormularioEdicion(cliente) }
        }

        val btnEliminar = ImageButton(this).apply {
            setImageResource(R.drawable.ic_baseline_delete_24)
            background = null
            setOnClickListener { confirmarEliminacion(cliente) }
        }

        val btnInfo = ImageButton(this).apply {
            setImageResource(R.drawable.ic_baseline_info_24)
            background = null
            setOnClickListener { verDetalles(cliente) }
        }

        layout.addView(btnEditar)
        layout.addView(btnEliminar)
        layout.addView(btnInfo)

        return layout
    }

    private fun mostrarFormularioEdicion(cliente: Clientes) {
        clienteSeleccionado = cliente

        val layoutEdicion = findViewById<View>(R.id.layoutActualizarCliente)
        layoutEdicion.visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombreActualizar).setText(cliente.nombreCliente)
        findViewById<EditText>(R.id.editTextApellidoActualizar).setText(cliente.apellidoCliente)
        findViewById<EditText>(R.id.editTextTelefonoActualizar).setText(cliente.telefonoCliente)
    }

    fun actualizarCliente(v: View) {
        clienteSeleccionado?.let { cliente ->
            val nombre = findViewById<EditText>(R.id.editTextNombreActualizar).text.toString()
            val apellido = findViewById<EditText>(R.id.editTextApellidoActualizar).text.toString()
            val telefono = findViewById<EditText>(R.id.editTextTelefonoActualizar).text.toString()

            clientesController.actualizarCliente(
                cliente.dniCliente.toString(),
                nombre,
                telefono,
                apellido
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Cliente actualizado correctamente")
                        findViewById<View>(R.id.layoutActualizarCliente).visibility = View.GONE
                        listarClientes(v)
                    } else {
                        mostrarToast("Error al actualizar cliente")
                    }
                }
            }
        }
    }


    private fun confirmarEliminacion(cliente: Clientes) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar cliente")
            .setMessage("¿Deseas eliminar este cliente?")
            .setPositiveButton("Eliminar") { _, _ ->
                clientesController.eliminarCliente(cliente.dniCliente.toString()) { success ->
                    runOnUiThread {
                        if (success) {
                            mostrarToast("Cliente eliminado")
                            listarClientes(findViewById(R.id.btnListarClientes))
                        } else {
                            mostrarToast("Error al eliminar cliente")
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(cliente: Clientes) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Cliente")
            .setMessage(
                """
                DNI: ${cliente.dniCliente}
                Nombre: ${cliente.nombreCliente}
                Apellido: ${cliente.apellidoCliente}
                Teléfono: ${cliente.telefonoCliente}
                """.trimIndent()
            )
            .setPositiveButton("Cerrar", null)
            .show()
    } fun buscarClientePorDni(v: View) {
        val dni = findViewById<EditText>(R.id.editTextBuscarDni).text.toString().trim()
        if (dni.isEmpty()) {
            mostrarToast("Ingrese un DNI para buscar")
            return
        }

        clientesController.buscarClientePorDni(dni) { cliente ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableClientes)
                table.removeViews(1, table.childCount - 1)

                if (cliente != null) {
                    val row = TableRow(this).apply {
                        addView(createCell(cliente.nombreCliente))  // Cambié 'nombre' por 'nombreCliente'
                        addView(createCell(cliente.apellidoCliente))  // Cambié 'apellido' por 'apellidoCliente'
                        addView(createCell(cliente.telefonoCliente))  // Cambié 'telefono' por 'telefonoCliente'
                        addView(crearBotonesAccion(cliente))
                    }

                    table.addView(row)
                } else {
                    mostrarToast("Cliente no encontrado")
                }
            }
        }
    }


    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.editTextDni).text.clear()
        findViewById<EditText>(R.id.editTextNombre).text.clear()
        findViewById<EditText>(R.id.editTextApellido).text.clear()
        findViewById<EditText>(R.id.editTextTelefono).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
