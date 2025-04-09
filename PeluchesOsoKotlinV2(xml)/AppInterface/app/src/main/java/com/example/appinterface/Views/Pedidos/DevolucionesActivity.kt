package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.DevolucionesController
import Models.Pedidos.Devoluciones
import Models.Productos.Catalogo
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R
import java.util.*

class DevolucionesActivity : AppCompatActivity() {
    private val devolucionesController = DevolucionesController()
    private var devolucionSeleccionada: Devoluciones? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devoluciones)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }


    fun crearDevolucion(v: View) {
        val numDevolucion = findViewById<EditText>(R.id.numDevolucion).text.toString()
        val detalleDevolucion = findViewById<EditText>(R.id.detalleDevolucion).text.toString()
        val fechaDevolucion = findViewById<EditText>(R.id.fechaDevolucion).text.toString()


        if (numDevolucion.isEmpty() || detalleDevolucion.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        devolucionesController.crearDevolucion(numDevolucion, detalleDevolucion, fechaDevolucion, productos = null) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Devolucion creada con exito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear la devolucion", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun listarDevoluciones(v: View) {
        devolucionesController.listarDevoluciones { devoluciones ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableDevoluciones)
                table.removeViews(1, table.childCount - 1)
                devoluciones?.forEach { devoluciones ->
                    val row = TableRow(this).apply {
                        addView(createCell(devoluciones.fechaDevolucion))
                        addView(createCell(devoluciones.detalleDevolucion))
                        addView(crearBotonesAccion(devoluciones))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar devoluciones")
            }
        }
    }


    // Función para buscar una devolución por ID
    //fun buscarDevolucionPorId(v: View) {
    //    val idDevolucion = findViewById<EditText>(R.id.idDevolucionBuscar).text.toString()
    //    val mensaje = devolucionesController.buscarDevolucionPorId(idDevolucion)
    //    findViewById<TextView>(R.id.textViewListadoDevoluciones).text = mensaje
    //}


    fun actualizarDevolucion(v: View) {
        devolucionSeleccionada?.let {devoluciones ->
            val nuevoNumDetalle = findViewById<EditText>(R.id.editTextNum).text.toString()
            val nuevoDetalleDevolucion = findViewById<EditText>(R.id.editTextDetalle).text.toString()
            val nuevaFechaDevolucion = findViewById<EditText>(R.id.editTextFecha).text.toString()


            devolucionesController.actualizarDevolucion(
                devoluciones.id ?: return,
                nuevoNumDetalle,
                nuevoDetalleDevolucion,
                nuevaFechaDevolucion
            ) { success -> runOnUiThread {
                if(success) {
                    mostrarToast("Actualizado correctamente")
                    findViewById<ConstraintLayout>(R.id.layoutActualizarDevolucion).visibility = View.GONE
                    listarDevoluciones(findViewById(R.id.listarDevoluciones))
                } else {
                    mostrarToast("Error al actualizar")
                }
            }}
        }
    }
    // Función para eliminar una devolución por ID
    fun confirmarEliminacion(devoluciones: Devoluciones) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar devolución")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ -> devoluciones.id?.let {id ->
                devolucionesController.eliminarDevolucion(id) { success -> runOnUiThread {
                    if(success) {
                        mostrarToast("Eliminado")
                        listarDevoluciones(findViewById(R.id.listarDevoluciones))
                    } else {
                        mostrarToast("Error al eliminar")
                    }
                }}
            }}
            .setNegativeButton("Cancelar", null)
            .show()
    }


    private fun crearBotonesAccion(devoluciones: Devoluciones): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(devoluciones)}
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(devoluciones)}
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(devoluciones)}
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(devoluciones: Devoluciones) {
        devolucionSeleccionada = devoluciones
        findViewById<ConstraintLayout>(R.id.layoutActualizarDevolucion).visibility = View.VISIBLE
        findViewById<EditText>(R.id.editTextNum).setText(devoluciones.numDevolucion)
        findViewById<EditText>(R.id.editTextDetalle).setText(devoluciones.detalleDevolucion)
        findViewById<EditText>(R.id.editTextFecha).setText(devoluciones.fechaDevolucion)
    }

    private fun verDetalles(devoluciones: Devoluciones) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Detalles de la Devolución")
            .setMessage("""
             Numero del detalle: ${devoluciones.numDevolucion}
             Detalle: ${devoluciones.detalleDevolucion}
             Fecha del detalle: ${devoluciones.fechaDevolucion}
         """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
