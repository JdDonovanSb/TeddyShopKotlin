package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.FacturaController
import Models.Pedidos.Factura
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R

class FacturasActivity : AppCompatActivity() {
    private val facturaController = FacturaController()
    private var facturaSeleccionadaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas)
        listarFacturas(findViewById(R.id.listarFacturas))
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearFactura(v: View) {
        val fecha = findViewById<EditText>(R.id.fechaFactura).text.toString().trim()
        val hora = findViewById<EditText>(R.id.horaFactura).text.toString().trim()

        if (fecha.isEmpty() || hora.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        facturaController.crearFactura(fecha, hora) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Factura creada con éxito")
                    listarFacturas(v)
                    limpiarFormulario()
                } else {
                    mostrarToast("Error al crear factura")
                }
            }
        }
    }

    fun listarFacturas(v: View) {
        facturaController.listarFacturas { facturas ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableFacturas)
                table.removeViews(1, table.childCount - 1)

                facturas?.forEach { factura ->
                    val row = TableRow(this).apply {
                        addView(createCell(factura.fechaCreacionFactura))
                        addView(createCell(factura.horaCreacionFactura))
                        addView(crearBotonesAccion(factura))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar facturas")
            }
        }
    }

    private fun crearBotonesAccion(factura: Factura): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)

            val btnEditar = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(factura) }
                background = null
            }
            addView(btnEditar)

            val btnEliminar = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(factura) }
                background = null
            }
            addView(btnEliminar)

            val btnDetalles = ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(factura) }
                background = null
            }
            addView(btnDetalles)
        }
    }

    private fun mostrarFormularioEdicion(factura: Factura) {
        facturaSeleccionadaId = factura.id
        findViewById<View>(R.id.layoutActualizarFactura).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextFechaFactura).setText(factura.fechaCreacionFactura)
        findViewById<EditText>(R.id.editTextHoraFactura).setText(factura.horaCreacionFactura)
    }

    fun actualizarFactura(v: View) {
        facturaSeleccionadaId?.let { id ->
            val nuevaFecha = findViewById<EditText>(R.id.editTextFechaFactura).text.toString().trim()
            val nuevaHora = findViewById<EditText>(R.id.editTextHoraFactura).text.toString().trim()

            if (nuevaFecha.isEmpty() || nuevaHora.isEmpty()) {
                mostrarToast("Complete todos los campos")
                return
            }

            facturaController.actualizarFactura(id, nuevaFecha, nuevaHora) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Factura actualizada correctamente")
                        findViewById<View>(R.id.layoutActualizarFactura).visibility = View.GONE
                        listarFacturas(v)
                    } else {
                        mostrarToast("Error al actualizar factura")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(factura: Factura) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar factura")
            .setMessage("¿Deseas eliminar esta factura?")
            .setPositiveButton("Eliminar") { _, _ ->
                factura.id?.let { id ->
                    facturaController.eliminarFactura(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Factura eliminada correctamente")
                                listarFacturas(findViewById(R.id.listarFacturas))
                            } else {
                                mostrarToast("Error al eliminar factura")
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun verDetalles(factura: Factura) {
        AlertDialog.Builder(this)
            .setTitle("Detalles de la Factura")
            .setMessage("Fecha: ${factura.fechaCreacionFactura}\nHora: ${factura.horaCreacionFactura}")
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.fechaFactura).text.clear()
        findViewById<EditText>(R.id.horaFactura).text.clear()
        findViewById<EditText>(R.id.editTextFechaFactura).text.clear()
        findViewById<EditText>(R.id.editTextHoraFactura).text.clear()
        findViewById<View>(R.id.layoutActualizarFactura).visibility = View.GONE
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
