package Controllers.Productos

import ConexionBD
import Models.Productos.Producto
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

class ProductoController {
    private val conexionBD = ConexionBD()

    // Crear un producto
    fun crearProducto(): String {

        println("Ingrese el ID del Producto: ")
        val idProducto = readln()

        println("Ingresa el estilo del producto:")
        val estiloProducto = readLine() ?: ""

        println("Ingresa el material del producto:")
        val materialProducto = readLine() ?: ""

        println("Ingresa el tamaño del producto:")
        val tamañoProducto = readLine() ?: ""

        println("¿Está disponible el producto? (true/false):")
        val disponibilidadProducto = readLine()?.toBoolean() ?: false

        val query = "INSERT INTO Producto (idProducto, estiloProducto, materialProducto, tamañoProducto, disponibilidadProducto) VALUES (?, ?, ?, ?, ?)"
        val conn = conexionBD.getConnection()

        conn?.use {
            try{
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, idProducto)
                    stmt.setString(2, estiloProducto)
                    stmt.setString(3, materialProducto)
                    stmt.setString(4, tamañoProducto)
                    stmt.setBoolean(5, disponibilidadProducto)
                    stmt.executeUpdate()
                }
                return "Producto creado con éxito."
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al crear el Producto"
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    // Listar todos los productos
    fun listarProductos(): String {
        val productos = mutableListOf<Producto>()
        val query = "SELECT * FROM Producto"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    val rs: ResultSet = stmt.executeQuery()
                    while (rs.next()) {
                        val idProducto = rs.getString("idProducto")
                        val estiloProducto = rs.getString("estiloProducto")
                        val materialProducto = rs.getString("materialProducto")
                        val tamañoProducto = rs.getString("tamañoProducto")
                        val disponibilidadProducto = rs.getBoolean("disponibilidadProducto")

                        val producto = Producto(idProducto, estiloProducto, materialProducto, tamañoProducto, disponibilidadProducto)
                        productos.add(producto)
                    }
                }
                return if(productos.isEmpty()) {
                    "No hay Productos disponibles."
                } else {
                    productos.joinToString ("\n") {
                        "ID: ${it.getIdProducto()}, Estilo: ${it.getEstiloProducto()}, Material: ${it.getMaterialProducto()}, Tamaño: ${it.getTamañoProducto()}, Disponibiildad: ${it.getDisponibilidadProducto()}"
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al obtener los Productos"
            }
        }
        return "No se pudo establecer la conexión con la base de datos."
    }

    // Buscar un producto por ID
    fun buscarProductoPorId(estiloProducto: String): String {
        val query = "SELECT * FROM Producto WHERE estiloProducto = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, estiloProducto)
                    val rs: ResultSet = stmt.executeQuery()
                    return if (rs.next()) {
                        val materialProducto = rs.getString("materialProducto")
                        val tamañoProducto = rs.getString("tamañoProducto")
                        val disponibilidadProducto = rs.getBoolean("disponibilidadProducto")
                        "Producto encontrado: Estilo: $estiloProducto, Material: $materialProducto, Tamaño: $tamañoProducto, Disponibilidad: $disponibilidadProducto"
                    } else {
                        "Producto no encontrado."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al buscar el Producto."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    // Actualizar un producto por ID
    fun actualizarProducto(estiloProducto: String): String {
        println("Ingresa el nuevo estilo del producto:")
        val nuevoEstiloProducto = readLine() ?: ""

        println("Ingresa el nuevo material del producto:")
        val nuevoMaterialProducto = readLine() ?: ""

        println("Ingresa el nuevo tamaño del producto:")
        val nuevoTamañoProducto = readLine() ?: ""

        println("¿Está disponible el producto? (true/false):")
        val nuevaDisponibilidadProducto = readLine()?.toBoolean() ?: false

        val query = "UPDATE Producto SET estiloProducto = ?, materialProducto = ?, tamañoProducto = ?, disponibilidadProducto = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nuevoEstiloProducto)
                    stmt.setString(2, nuevoMaterialProducto)
                    stmt.setString(3, nuevoTamañoProducto)
                    stmt.setBoolean(4, nuevaDisponibilidadProducto)
                    val filasActualizadas = stmt.executeUpdate()

                    return if (filasActualizadas > 0) {
                        "Producto actualizado con éxito."
                    } else {
                        "No se encontró el Producto a actualizar."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error al actualizar la categoría."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    // Eliminar un producto por ID
    fun eliminarProducto(estiloProducto: String): String {
        val query = "DELETE FROM Producto WHERE estiloProducto = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, estiloProducto)
                    val filasEliminadas = stmt.executeUpdate()
                    return if (filasEliminadas > 0) {
                        "Producto eliminado con exito."
                    } else {
                        "No se encontro el Producto a eliminar."
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al eliminar el Producto."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }
}