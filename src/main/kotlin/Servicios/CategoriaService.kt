package Servicios


import Controllers.Productos.CategoriaController


class CategoriaService {
    fun AdminCategorias() {
        val categoriaController = CategoriaController()


            val opcion = readLine()?.toIntOrNull()

            while (true) {
                println("\nGestión de Categorías:")
                println("1. Crear Categoría")
                println("2. Listar Categorías")
                println("3. Buscar Categoría")
                println("4. Actualizar Categoría")
                println("5. Eliminar Categoría")
                println("6. Salir")
                print("Seleccione una opción: ")

                when (readln().toInt()) {
                    1 -> println(categoriaController.crearCategoria())
                    2 -> println(categoriaController.listarCategorias())
                    3 -> {
                        println("Ingrese el nombre de la categoría:")
                        val nombre = readln()
                        println(categoriaController.buscarCategoria(nombre))
                    }
                    4 -> {
                        println("Ingrese el nombre de la categoría a actualizar:")
                        val nombre = readln()
                        println(categoriaController.actualizarCategoria(nombre))
                    }
                    5 -> {
                        println("Ingrese el nombre de la categoría a eliminar:")
                        val nombre = readln()
                        println(categoriaController.eliminarCategoria(nombre))
                    }
                    6 -> {
                        println("Saliendo...")
                        break
                    }
                    else -> println("Opción no válida, intente de nuevo.")
                }
            }
    }
}
