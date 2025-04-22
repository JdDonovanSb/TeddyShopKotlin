package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.DetalleFacturaController
import Models.Pedidos.DetalleFactura
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class DetalleFacturaActivity : AppCompatActivity() {
    private val controller = DetalleFacturaController()
    private var detalleSeleccionado: DetalleFactura? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detallefactura)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearDetalleFactura(v: View) {
        val numDetalle = findViewById<EditText>(R.id.numDetalle).text.toString().toIntOrNull()
        val precio = findViewById<EditText>(R.id.precioDetalle).text.toString().toDoubleOrNull()
        val cantidad = findViewById<EditText>(R.id.cantidadDetalle).text.toString().toIntOrNull()

        if (numDetalle == null || precio == null || cantidad == null) {
            mostrarToast("Complete todos los campos correctamente")
            return
        }

        controller.crearDetalleFactura(numDetalle, precio, cantidad) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Detalle creado con éxito")
                    limpiarFormulario()
                } else {
                    mostrarToast("Error al crear detalle")
                }
            }
        }
    }

    fun listarDetallesFactura(v: View) {
        controller.listarDetallesFactura { detalles ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableDetallesFactura)
                table.removeViews(1, table.childCount - 1)

                detalles?.forEach { detalle ->
                    val row = TableRow(this).apply {
                        addView(createCell(detalle.numDetalle.toString()))
                        addView(createCell("$${detalle.precioDetalleFactura}"))
                        addView(createCell(detalle.cantidadDetalleFactura.toString()))
                        addView(crearBotonesAccion(detalle))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar detalles")
            }
        }
    }

    private fun crearBotonesAccion(detalle: DetalleFactura): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(detalle) }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(detalle) }
                background = null
                addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(detalle) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(detalle: DetalleFactura) {
        detalleSeleccionado = detalle
        // Asegúrate de que la vista a la que estás accediendo es del tipo adecuado.
        val layoutActualizarDetalle = findViewById<LinearLayout>(R.id.layoutActualizarDetalle) // Cambiado a LinearLayout si es necesario
        layoutActualizarDetalle.visibility = View.VISIBLE

        findViewById<EditText>(R.id.editNumDetalle).setText(detalle.numDetalle.toString())
        findViewById<EditText>(R.id.editPrecioDetalle).setText(detalle.precioDetalleFactura.toString())
        findViewById<EditText>(R.id.editCantidadDetalle).setText(detalle.cantidadDetalleFactura.toString())
    }

    fun actualizarDetalleFactura(v: View) {
        detalleSeleccionado?.let { detalle ->
            val nuevoNum = findViewById<EditText>(R.id.editNumDetalle).text.toString().toIntOrNull()
            val nuevoPrecio = findViewById<EditText>(R.id.editPrecioDetalle).text.toString().toDoubleOrNull()
            val nuevaCantidad = findViewById<EditText>(R.id.editCantidadDetalle).text.toString().toIntOrNull()

            if (nuevoNum == null || nuevoPrecio == null || nuevaCantidad == null) {
                mostrarToast("Datos inválidos")
                return
            }

            controller.actualizarDetalleFactura(
                detalle.id ?: return,
                nuevoNum,
                nuevoPrecio,
                nuevaCantidad
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<LinearLayout>(R.id.layoutActualizarDetalle).visibility = View.GONE // Cambiado a LinearLayout si es necesario
                        listarDetallesFactura(findViewById(R.id.listarDetallesFactura))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(detalle: DetalleFactura) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar detalle")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                detalle.id?.let { id ->
                    controller.eliminarDetalleFactura(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminado")
                                listarDetallesFactura(findViewById(R.id.listarDetallesFactura))
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

    private fun verDetalles(detalle: DetalleFactura) {
        AlertDialog.Builder(this)
            .setTitle("Detalles completos")
            .setMessage("""
                Número: ${detalle.numDetalle}
                Precio: $${detalle.precioDetalleFactura}
                Cantidad: ${detalle.cantidadDetalleFactura}
                ID: ${detalle.id}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.numDetalle).text.clear()
        findViewById<EditText>(R.id.precioDetalle).text.clear()
        findViewById<EditText>(R.id.cantidadDetalle).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
