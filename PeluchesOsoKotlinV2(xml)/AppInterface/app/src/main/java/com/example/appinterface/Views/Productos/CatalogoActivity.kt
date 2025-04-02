package com.example.appinterface.Views.Productos

import Controller.Productos.CatalogoController
import Models.Productos.Catalogo
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class CatalogoActivity : AppCompatActivity() {
    private val catalogoController = CatalogoController()
    private var catalogoSeleccionado: Catalogo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearCatalogo(v: View) {
        val nombre = findViewById<EditText>(R.id.nombreCatalogo).text.toString().trim()
        val descripcion = findViewById<EditText>(R.id.descripcionCatalogo).text.toString().trim()
        val disponible = findViewById<Switch>(R.id.disponibilidadCatalogo).isChecked
        val estilo = findViewById<EditText>(R.id.estiloCatalogo).text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty() || estilo.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        catalogoController.crearCatalogo(nombre, descripcion, disponible, estilo, imagen = null, productos = null, companias = null ) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Catalogo creada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al crear Catalogo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun listarCatalogos(v: View) {
        catalogoController.listarCatalogos { catalogos ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableCatalogos)
                table.removeViews(1, table.childCount - 1)

                catalogos?.forEach { catalogo ->
                    val row = TableRow(this).apply {
                        addView(createCell(catalogo.nombreCatalogo))
                        addView(createCell(if (catalogo.disponibilidadCatalogo) "Sí" else "No"))
                        addView(crearBotonesAccion(catalogo))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar catálogos")
            }
        }
    }

    private fun crearBotonesAccion(catalogo: Catalogo): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(catalogo) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(catalogo) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(catalogo) }
                background = null
                addView(this)
            }


        }
    }

    private fun mostrarFormularioEdicion(catalogo: Catalogo) {
        catalogoSeleccionado = catalogo
        findViewById<ConstraintLayout>(R.id.layoutActualizarCatalogo).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombre).setText(catalogo.nombreCatalogo)
        findViewById<EditText>(R.id.editTextDescripcion).setText(catalogo.descripcionCatalogo)
        findViewById<EditText>(R.id.editTextEstilo).setText(catalogo.estiloCatalogo)
        findViewById<Switch>(R.id.switchDisponibilidad).isChecked = catalogo.disponibilidadCatalogo
    }

    fun actualizarCatalogo(v: View) {
        catalogoSeleccionado?.let { catalogo ->
            val nuevoNombre = findViewById<EditText>(R.id.editTextNombre).text.toString()
            val nuevaDescripcion = findViewById<EditText>(R.id.editTextDescripcion).text.toString()
            val nuevoEstilo = findViewById<EditText>(R.id.editTextEstilo).text.toString()
            val nuevaDisponibilidad = findViewById<Switch>(R.id.switchDisponibilidad).isChecked

            catalogoController.actualizarCatalogo(
                catalogo.id ?: return,
                nuevoNombre,
                nuevaDescripcion,
                nuevaDisponibilidad,
                nuevoEstilo
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<ConstraintLayout>(R.id.layoutActualizarCatalogo).visibility = View.GONE
                        listarCatalogos(findViewById(R.id.listarCatalogos))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }

    }

    private fun confirmarEliminacion(catalogo: Catalogo) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar catálogo")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                catalogo.id?.let { id ->
                    catalogoController.eliminarCatalogo(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminado")
                                listarCatalogos(findViewById(R.id.listarCatalogos))
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

    private fun verDetalles(catalogo: Catalogo) {
        AlertDialog.Builder(this)
            .setTitle("Detalles del Catálogo")
            .setMessage("""
             Nombre: ${catalogo.nombreCatalogo}
             Descripción: ${catalogo.descripcionCatalogo}
         """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.nombreCatalogo).text.clear()
        findViewById<EditText>(R.id.descripcionCatalogo).text.clear()
        findViewById<EditText>(R.id.estiloCatalogo).text.clear()
        findViewById<Switch>(R.id.disponibilidadCatalogo).isChecked = false
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
