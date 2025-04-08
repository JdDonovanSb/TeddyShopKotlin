package com.example.appinterface.Views.Productos

import Controller.Productos.CategoriaController
import Models.Productos.Categoria
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appinterface.R

class CategoriaActivity : AppCompatActivity() {
    private val categoriaController = CategoriaController()
    private var categoriaSeleccionada: Categoria? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    fun crearCategoria(v: View) {
        val nombre = findViewById<EditText>(R.id.nombreCategoria).text.toString().trim()
        val descripcion = findViewById<EditText>(R.id.descripcionCategoria).text.toString().trim()

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarToast("Complete todos los campos")
            return
        }

        categoriaController.crearCategoria(nombre, descripcion, imagen = null, productos = null) { success ->
            runOnUiThread {
                if (success) {
                    mostrarToast("Categoría creada con éxito")
                    limpiarFormulario()
                } else {
                    mostrarToast("Error al crear categoría")
                }
            }
        }
    }

    fun listarCategorias(v: View) {
        categoriaController.listarCategorias { categorias ->
            runOnUiThread {
                val table = findViewById<TableLayout>(R.id.tableCategorias)
                table.removeViews(1, table.childCount - 1)

                categorias?.forEach { categoria ->
                    val row = TableRow(this).apply {
                        addView(createCell(categoria.nombre))
                        addView(createCell(categoria.descripcion))
                        addView(crearBotonesAccion(categoria))
                    }
                    table.addView(row)
                } ?: mostrarToast("Error al cargar categorías")
            }
        }
    }

    private fun crearBotonesAccion(categoria: Categoria): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f)
            gravity = Gravity.CENTER

            // Botón Editar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_edit_24)
                setOnClickListener { mostrarFormularioEdicion(categoria) }
                background = null
                addView(this)
            }

            // Botón Eliminar
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_delete_24)
                setOnClickListener { confirmarEliminacion(categoria) }
                background = null
                addView(this)
            }

            // Botón Detalles
            ImageButton(context).apply {
                setImageResource(R.drawable.ic_baseline_info_24)
                setOnClickListener { verDetalles(categoria) }
                background = null
                addView(this)
            }
        }
    }

    private fun mostrarFormularioEdicion(categoria: Categoria) {
        categoriaSeleccionada = categoria
        findViewById<ConstraintLayout>(R.id.layoutActualizarCategoria).visibility = View.VISIBLE

        findViewById<EditText>(R.id.editTextNombre).setText(categoria.nombre)
        findViewById<EditText>(R.id.editTextDescripcion).setText(categoria.descripcion)
    }

    fun actualizarCategoria(v: View) {
        categoriaSeleccionada?.let { categoria ->
            val nuevoNombre = findViewById<EditText>(R.id.editTextNombre).text.toString()
            val nuevaDescripcion = findViewById<EditText>(R.id.editTextDescripcion).text.toString()

            categoriaController.actualizarCategoria(
                categoria.id ?: return,
                nuevoNombre,
                nuevaDescripcion,
                null
            ) { success ->
                runOnUiThread {
                    if (success) {
                        mostrarToast("Actualizado correctamente")
                        findViewById<ConstraintLayout>(R.id.layoutActualizarCategoria).visibility = View.GONE
                        listarCategorias(findViewById(R.id.listarCategorias))
                    } else {
                        mostrarToast("Error al actualizar")
                    }
                }
            }
        }
    }

    private fun confirmarEliminacion(categoria: Categoria) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar categoría")
            .setMessage("¿Confirmar eliminación?")
            .setPositiveButton("Eliminar") { _, _ ->
                categoria.id?.let { id ->
                    categoriaController.eliminarCategoria(id) { success ->
                        runOnUiThread {
                            if (success) {
                                mostrarToast("Eliminada correctamente")
                                listarCategorias(findViewById(R.id.listarCategorias))
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

    private fun verDetalles(categoria: Categoria) {
        AlertDialog.Builder(this)
            .setTitle("Detalles de la Categoría")
            .setMessage("""
                Nombre: ${categoria.nombre}
                Descripción: ${categoria.descripcion}
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun limpiarFormulario() {
        findViewById<EditText>(R.id.nombreCategoria).text.clear()
        findViewById<EditText>(R.id.descripcionCategoria).text.clear()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}