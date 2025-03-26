package Controller.Productos

import ConexionBD
import Models.Productos.Catalogo
import Models.Productos.HistorialPrecio
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

//import java.util.*

class CatalogoController {
    private val conexionBD = ConexionBD()

    //Crear un nuevo catalogo
    fun crearCatalogo(): String {
        println("Ingrese el ID del Catalogo: ")
        val idCatalogo = readln()

        println("Ingrese el nombre del catalogo")
        val nombreCatalogo = readLine() ?: ""

        println("Ingrese la descripcion del catalogo")
        val descripcionCatalogo = readLine() ?: ""

        println("Ingrese la la disponibilidad del catalogo (true/false)")
        val disponibilidadCatalogo = readLine()?.toBoolean() ?: false

        println("Ingrese el estilo del catalogo")
        val estiloCatalogo = readLine() ?: ""

        val query = "INSERT INTO Catalogo (IdCatalogo, EstiloCatalogo, DescripcionCatalogo, DisponibilidadCatalogo, NombreCatalogo) VALUES (?, ?, ?, ?, ?)"
        val conn = conexionBD.getConnection()

        conn?.use {
            try{
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, idCatalogo)
                    stmt.setString(2, estiloCatalogo)
                    stmt.setString(3, descripcionCatalogo)
                    stmt.setBoolean(4, disponibilidadCatalogo)
                    stmt.setString(5, nombreCatalogo)
                    stmt.executeUpdate()
                }
                return "Catalogo creado con exito."
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al crear el catalogo"
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }

    //LIstar Catalogos
    fun listarCatalogos(): String {
        val catalogos = mutableListOf<Catalogo>()
        val query = "SELECT * FROM Catalogo"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    val rs: ResultSet = stmt.executeQuery()
                    while (rs.next()) {
                        val id = rs.getString("IdCatalogo")
                        val nombre = rs.getString("EstiloCatalogo")
                        val descripcion = rs.getString("DescripcionCatalogo")
                        val disponibilidad = rs.getBoolean("DisponibilidadCatalogo")
                        val estilo = rs.getString("NombreCatalogo")

                        val catalogo = Catalogo(id, estilo, descripcion, disponibilidad, nombre)
                        catalogos.add(catalogo)
                    }
                }
                return if(catalogos.isEmpty()) {
                    "No hay catalogos disponibles."
                } else {
                    catalogos.joinToString ("\n") {
                        "ID: ${it.getIdCatalogo()}, Nombre: ${it.getNombreCatalogo()}, Descripcion: ${it.getDescripcionCatalogo()}, Disponibiildad: ${it.getDisponibilidadCatalogo()}, Estilo: ${it.getEstiloCatalogo()}"
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al obtener los catalogos"
            }
        }
        return "No se pudo establecer la conexión con la base de datos."
    }

    //Buscar un Catalogo de precio por ID
    fun buscarCatalogoPorId(nombre: String): String {
       val query = "SELECT * FROM Catalogo WHERE NombreCatalogo = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nombre)
                    val rs: ResultSet = stmt.executeQuery()
                    return if (rs.next()) {
                        val descripcion = rs.getString("DescripcionCatalogo")
                        val disponibilidad = rs.getBoolean("DisponibilidadCatalogo")
                        val estilo = rs.getString("EstiloCatalogo")
                        "Catalogo encontrado: Nombre: $nombre, Descripcion: $descripcion, Disponibilidad: $disponibilidad, Estilo: $estilo"
                    } else {
                        "Catalogo no encontrado."
                    }
                }
            } catch (e: SQLException){
                e.printStackTrace()
                return "Error al buscar el catalogo"
            }
        }
        return "No se pudo establecer conexion a la base de datos"
    }

    // Actualizar un Catalogo por ID
    fun actualizarCatalogo(nombre: String): String {
        println("Ingresa el nuevo Nombre:")
        val nuevoNombre = readLine()

        println("Ingresa la nueva descripción:")
        val nuevaDescripcion = readLine()

        println("¿Está disponible el catalogo? (true/false):")
        val nuevoEstado = readLine().toBoolean()

        println("Ingresa el nuevo etilo del catalogo:")
        val nuevoEstilo = readLine()

        val query = "UPDATE Catalogo SET NombreCatalogo = ?, DescripcionCatalogo = ?, DisponibilidadCatalogo = ?, EstiloCatalogo = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nuevoNombre)
                    stmt.setString(2, nuevaDescripcion)
                    stmt.setBoolean(3, nuevoEstado)
                    stmt.setString(4, nuevoEstilo)
                    val filasActualizadas = stmt.executeUpdate()

                    return if (filasActualizadas > 0) {
                        "Catalogo actualizado con exito."
                    } else {
                        "No se encontro el catalogo a actualizar."
                    }
                }
            } catch (e: SQLException){
                e.printStackTrace()
                return "Error al actualizar el catalogo"
            }
        }
        return "No se pudo establecer la conexión con la base de datos."
    }

    // Eliminar un Catalogo por ID
    fun eliminarCatalogo(nombre: String): String {
        val query = "DELETE FROM Catalogo WHERE NombreCatalogo = ?"
        val conn = conexionBD.getConnection()

        conn?.use {
            try {
                it.prepareStatement(query).use { stmt ->
                    stmt.setString(1, nombre)
                    val filasEliminadas = stmt.executeUpdate()
                    return if (filasEliminadas > 0) {
                        "Catalogo eliminado con exito."
                    } else {
                        "No se encontro el catalogo a eliminar."
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                return "Error al eliminar el catalogo."
            }
        }
        return "No se pudo establecer conexión con la base de datos."
    }
}