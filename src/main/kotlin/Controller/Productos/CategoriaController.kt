package Controllers.Productos

import Models.Productos.Categoria
import ConexionBD
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class CategoriaController {
    private val conexionBD = ConexionBD()  // Instancia para manejar la conexión

    fun crearCategoria(): String {

        println("Ingrese el ID de la categoria: ")
        val id = readln()

        println("Ingrese el nombre de la categoría: ")
        val nombre = readln()

        println("Ingrese la descripción de la categoría: ")
        val descripcion = readln()


        val query = "INSERT INTO Categoria (IdCategoria, NombreCategoria, DescripcionCategoria) VALUES (?, ?, ?)"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, id)
                    stmt.setString(2, nombre)
                    stmt.setString(3, descripcion)
                    stmt.executeUpdate()
                }
                return "Categoría creada con éxito."
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al crear la categoría."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    fun listarCategorias(): String {
        val categorias = mutableListOf<Categoria>()
        val query = "SELECT * FROM Categoria"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    val rs: ResultSet = stmt.executeQuery()
                    while (rs.next()) {
                        val id = rs.getString("IdCategoria")
                        val nombre = rs.getString("NombreCategoria")
                        val descripcion = rs.getString("DescripcionCategoria")
                        val categoria = Categoria(id, nombre, descripcion)
                        categorias.add(categoria)
                    }
                }
                return if (categorias.isEmpty()) {
                    "No hay categorías disponibles."
                } else {
                    categorias.joinToString("\n") {
                        "ID: ${it.getIdCategoria()}, Nombre: ${it.getNombreCategoria()}, Descripción: ${it.getDescripcionCategoria()}"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al obtener las categorías."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    fun buscarCategoria(nombre: String): String {
        val query = "SELECT * FROM Categoria WHERE NombreCategoria = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nombre)
                    val rs: ResultSet = stmt.executeQuery()
                    return if (rs.next()) {
                        val descripcion = rs.getString("DescripcionCategoria")
                        "Categoría encontrada: Nombre: $nombre, Descripción: $descripcion"
                    } else {
                        "Categoría no encontrada."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al buscar la categoría."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    fun actualizarCategoria(nombre: String): String {
        println("Ingrese el nuevo nombre para la categoría:")
        val nuevoNombre = readln()

        println("Ingrese la nueva descripción para la categoría:")
        val nuevaDescripcion = readln()

        val query = "UPDATE Categoria SET NombreCategoria = ?, DescripcionCategoria = ? WHERE NombreCategoria = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nuevoNombre)
                    stmt.setString(2, nuevaDescripcion)
                    stmt.setString(3, nombre)
                    val filasActualizadas = stmt.executeUpdate()
                    return if (filasActualizadas > 0) {
                        "Categoría actualizada con éxito."
                    } else {
                        "No se encontró la categoría a actualizar."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al actualizar la categoría."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    fun eliminarCategoria(nombre: String): String {
        val query = "DELETE FROM Categoria WHERE NombreCategoria = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nombre)
                    val filasEliminadas = stmt.executeUpdate()
                    return if (filasEliminadas > 0) {
                        "Categoría eliminada con éxito."
                    } else {
                        "No se encontró la categoría a eliminar."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al eliminar la categoría."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }
}
