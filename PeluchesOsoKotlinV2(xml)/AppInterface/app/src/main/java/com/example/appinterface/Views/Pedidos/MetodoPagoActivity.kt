package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.MetodoPagoController
import Models.Pedidos.MetodoPago
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class MetodoPagoActivity : AppCompatActivity() {
    private val metodoPagoController = MetodoPagoController()
    private var metodoSeleccionado: MetodoPago? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metodopago)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearMetodoPago(view: View) {
        val nombre = findViewById<EditText>(R.id.nombreMetodoPago).text.toString().trim()

        if (nombre.isEmpty()) {
            mostrarToast("Ingrese el nombre del método")
            return
        }

        val nuevoMetodo = MetodoPago(nombreMetodoPago = nombre)

        metodoPagoController.crearMetodoPago(nuevoMetodo) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Método de pago creado")
                    limpiarFormulario()
                    listarMetodosPago(findViewById(R.id.ListarMetodoPagos))
                } else {
                    mostrarToast("Error al crear método")
                }
            }
        }
    }

    fun listarMetodosPago(view: View) {
        metodoPagoController.listarMetodosPago { lista ->
            runOnUiThread {
                val tabla = findViewById<TableLayout>(R.id.tableMetodosPago)
                if (tabla.childCount > 1) {
                    tabla.removeViews(1, tabla.childCount - 1)
                }

                lista?.forEach { metodo ->
                    val row = TableRow(this).apply {
                        addView(createCell(metodo.nombreMetodoPago))
                        addView(crearBotonesAccion(metodo))
                    }
                    tabla.addView(row)
                } ?: mostrarToast("No se pudo cargar la lista")
            }
        }
    }

    private fun crearBotonesAccion(metodo: MetodoPago): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(metodo) }
                background = null
            }.also { addView(it) }

            // Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(metodo) }
                background = null
            }.also { addView(it) }

            // Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(metodo) }
                background = null
            }.also { addView(it) }
        }
    }

    private fun mostrarFormularioEdicion(metodo: MetodoPago) {
        metodoSeleccionado = metodo
        findViewById<LinearLayout>(R.id.layoutActualizarMetodoPago).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextNombreMetodoPago).setText(metodo.nombreMetodoPago)
    }

    fun actualizarMetodoPago(view: View) {
        val nuevoNombre = findViewById<EditText>(R.id.editTextNombreMetodoPago).text.toString().trim()

        if (nuevoNombre.isEmpty()) {
            mostrarToast("Ingrese un nuevo nombre")
            return
        }

        metodoSeleccionado?.let {
            it.nombreMetodoPago = nuevoNombre

            val nuevoMetodo = MetodoPago(
                nombreMetodoPago = nuevoNombre

            )
            metodoPagoController.actualizarMetodoPago(it.id ?: "", nuevoMetodo) { success ->


                runOnUiThread {
                    if (success) {
                        mostrarToast("Método actualizado")
                        findViewById<LinearLayout>(R.id.layoutActualizarMetodoPago).visibility = View.GONE
                        listarMetodosPago(findViewById(R.id.ListarMetodoPagos))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(metodo: MetodoPago) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar método de pago")
            .setMessage("¿Deseas eliminar este método de pago?")
            .setPositiveButton("Eliminar") { _, _ ->
                if (metodo.id.isNullOrEmpty()) {
                    mostrarToast("ID no válido")
                    return@setPositiveButton
                }
                metodoPagoController.eliminarMetodoPago(metodo.id) { success ->
                    runOnUiThread {
                        if (success) {
                            mostrarToast("Método eliminado")
                            listarMetodosPago(findViewById(R.id.ListarMetodoPagos))
                        } else {
                            mostrarToast("Error al eliminar")
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(metodo: MetodoPago) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del método de pago")
            .setMessage("Nombre: ${metodo.nombreMetodoPago}")
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.nombreMetodoPago).text.clear()
    }

    private fun mostrarToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
