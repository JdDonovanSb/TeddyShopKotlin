package com.example.appinterface.Views.Productos

import Controllers.Productos.ProductoController
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.R

class ProductoActivity : AppCompatActivity() {

    private val productoController = ProductoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
    }

    fun crearProducto(v: View) {
        val estilo = findViewById<EditText>(R.id.estiloProducto).text.toString().trim()
        val material = findViewById<EditText>(R.id.materialProducto).text.toString().trim()
        val tamaño = findViewById<EditText>(R.id.tamañoProducto).text.toString().trim()
        val disponibilidad = findViewById<EditText>(R.id.disponibilidadProducto).text.toString().trim()

        if (estilo.isEmpty() || material.isEmpty() || tamaño.isEmpty() || disponibilidad.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        productoController.crearProducto(estilo, material, tamaño, disponibilidad, null, null, null, null) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Producto creado con éxito")
                    limpiarFormulario()
                    listarProductos()
                } else {
                    mostrarToast("Error al crear producto")
                }
            }
        }
    }

    fun listarProductos(v: View? = null) {
        productoController.listarProductos { productos ->
            runOnUiThread {
                val textView = findViewById<TextView>(R.id.textViewListadoProductos)

                if (productos.isNullOrEmpty()) {
                    textView.text = "No se encontraron productos"
                } else {
                    val lista = productos.joinToString("\n\n") {
                        "ID: ${it.id}\nEstilo: ${it.estiloProducto}\nMaterial: ${it.materialProducto}\nTamaño: ${it.tamañoProducto}\nDisponible: ${it.disponibilidadProducto}"
                    }
                    textView.text = lista
                }
            }
        }
    }

    fun actualizarProducto(v: View) {
        val idTexto = findViewById<EditText>(R.id.idProductoActualizar).text.toString().trim()
        val estilo = findViewById<EditText>(R.id.nuevoEstiloProducto).text.toString().trim()
        val material = findViewById<EditText>(R.id.nuevoMaterialProducto).text.toString().trim()
        val tamaño = findViewById<EditText>(R.id.nuevoTamañoProducto).text.toString().trim()
        val disponibilidad = findViewById<EditText>(R.id.nuevaDisponibilidadProducto).text.toString().trim()

        if (idTexto.isEmpty() || estilo.isEmpty() || material.isEmpty() || tamaño.isEmpty() || disponibilidad.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        productoController.actualizarProducto(idTexto, estilo, material, tamaño, disponibilidad, null, null, null, null) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Producto actualizado")
                    listarProductos()
                } else {
                    mostrarToast("Error al actualizar")
                }
            }
        }
    }

    fun eliminarProducto(v: View) {
        val idTexto = findViewById<EditText>(R.id.idProductoEliminar).text.toString().trim()

        if (idTexto.isEmpty()) {
            mostrarToast("Ingrese un ID para eliminar")
            return
        }

        confirmarEliminacion(idTexto)
    }

    private fun confirmarEliminacion(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                productoController.eliminarProducto(id) { success ->
                    runOnUiThread {
                        if (success) {
                            mostrarToast("Producto eliminado")
                            listarProductos()
                        } else {
                            mostrarToast("Error al eliminar producto")
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.estiloProducto).text.clear()
        findViewById<EditText>(R.id.materialProducto).text.clear()
        findViewById<EditText>(R.id.tamañoProducto).text.clear()
        findViewById<EditText>(R.id.disponibilidadProducto).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
