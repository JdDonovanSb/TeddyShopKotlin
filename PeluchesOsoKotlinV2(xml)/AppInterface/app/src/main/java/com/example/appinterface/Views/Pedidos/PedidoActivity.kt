package com.example.appinterface.Views.Pedidos

import Controller.Pedidos.PedidoController
import Models.Pedidos.Pedido
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class PedidoActivity : AppCompatActivity() {

    private val pedidoController = PedidoController()
    private var pedidoSeleccionado: Pedido? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun CrearPedido(view: View) {
        val nombreComprador = findViewById<EditText>(R.id.nombreComprador).text.toString().trim()
        val numeroComprador = findViewById<EditText>(R.id.numeroComprador).text.toString().trim()
        val nombreAgendador = findViewById<EditText>(R.id.nombreAgendador).text.toString().trim()
        val numeroAgendador = findViewById<EditText>(R.id.numeroAgendador).text.toString().trim()
        val localidad = findViewById<EditText>(R.id.localidad).text.toString().trim()
        val direccion = findViewById<EditText>(R.id.direccion).text.toString().trim()
        val barrio = findViewById<EditText>(R.id.barrio).text.toString().trim()

        if (nombreComprador.isEmpty() || numeroComprador.isEmpty() || nombreAgendador.isEmpty() || numeroAgendador.isEmpty() || localidad.isEmpty() || direccion.isEmpty() || barrio.isEmpty()) {
            mostrarToast("Completee todos los campos")
            return
        }

        pedidoController.crearPedido(nombreComprador, numeroComprador, nombreAgendador, numeroAgendador, localidad, direccion, barrio, /*cliente = null, */detallesPedido = null, /*facturas = null*/) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Pedido creado con éxito")
                    limpiarFormularioCrear()
                    ListarPedidos(findViewById(R.id.ListarPedidos))
                } else {
                    mostrarToast("Error al crear el Pedido")
                }
            }
        }
    }

    fun ListarPedidos(v: View) {
        pedidoController.listarPedidos { pedidos ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tablePedidos)
                table.removeViews(1, table.childCount - 1)

                pedidos?.forEach { pedidos ->
                    val row = TableRow(this).apply {
                        addView(createCell(pedidos.nombreComprador))
                        addView(createCell(pedidos.direccion))
                        addView(crearBotonesAccion(pedidos))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar los Pedidos")
            }
        }
    }

    private fun crearBotonesAccion(pedido: Pedido): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            ImageButton(context).apply {
             setImageResource(R.drawable.ic_baseline_edit_24)
             setOnClickListener { mostrarFormularioEdicion(pedido) }
             background = null
             addView(this)
            }

            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(pedido) }
                background = null
                addView(this)
            }
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(pedido) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(pedido: Pedido) {
        pedidoSeleccionado = pedido
        findViewById<ConstraintLayout>(R.id.layoutActualizarPedido).visibility = View.VISIBLE

        findViewById<EditText>(R.id.nuevoNombreComprador).setText(pedido.nombreComprador)
        findViewById<EditText>(R.id.nuevoNumeroComprador).setText(pedido.numeroComprador)
        findViewById<EditText>(R.id.nuevoNombreAgendador).setText(pedido.nombreAgendador)
        findViewById<EditText>(R.id.nuevoNumeroAgendador).setText(pedido.numeroAgendador)
        findViewById<EditText>(R.id.nuevaLocalidad).setText(pedido.localidad)
        findViewById<EditText>(R.id.nuevaDireccion).setText(pedido.direccion)
        findViewById<EditText>(R.id.nuevoBarrio).setText(pedido.barrio)
    }

    fun ActualizarPedido(view: View) {
        pedidoSeleccionado?.let { pedido ->
            val nuevoNombreComprador = findViewById<EditText>(R.id.nuevoNombreComprador).text.toString()
            val nuevoNumeroComprador = findViewById<EditText>(R.id.nuevoNumeroComprador).text.toString()
            val nuevoNombreAgendador = findViewById<EditText>(R.id.nuevoNombreAgendador).text.toString()
            val nuevoNumeroAgendador = findViewById<EditText>(R.id.nuevoNumeroAgendador).text.toString()
            val nuevaLocalidad = findViewById<EditText>(R.id.nuevaLocalidad).text.toString()
            val nuevaDireccion = findViewById<EditText>(R.id.nuevaDireccion).text.toString()
            val nuevoBarrio = findViewById<EditText>(R.id.nuevoBarrio).text.toString()

            pedidoController.actualizarPedido(
                pedido.id ?: return,
                nuevoNombreComprador,
                nuevoNumeroComprador,
                nuevoNombreAgendador,
                nuevoNumeroAgendador,
                nuevaLocalidad,
                nuevaDireccion,
                nuevoBarrio
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Pedido actualizado")
                        findViewById<ConstraintLayout>(R.id.layoutActualizarPedido).visibility = View.GONE
                        ListarPedidos(findViewById(R.id.ListarPedidos))
                    } else {
                        mostrarToast("Error al Actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(pedido: Pedido) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Pedido")
            .setMessage("¿Seguro que deseas eliminar este pedido?")
            .setPositiveButton("Eliminar") { _, _ ->
                pedido.id?.let { id ->
                    pedidoController.eliminarPedido(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Pedido eliminado")
                                ListarPedidos(findViewById(R.id.ListarPedidos))

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

    private fun verDetalles(pedido: Pedido) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Pedido")
            .setMessage("""
             Nombre Comprador: ${pedido.nombreComprador}
             Numero Comprador: ${pedido.numeroComprador}
             Nombre Agendador: ${pedido.nombreAgendador}
             Numero Agendador: ${pedido.numeroAgendador}
             Localidad: ${pedido.localidad}
             Direccion: ${pedido.direccion}
             Barrio: ${pedido.barrio}
         """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormularioCrear() {
        val campos = listOf(
            R.id.nombreComprador, R.id.numeroComprador,
            R.id.nombreAgendador, R.id.numeroAgendador,
            R.id.localidad, R.id.direccion, R.id.barrio
        )
        campos.forEach { findViewById<EditText>(it).text.clear() }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
