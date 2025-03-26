package Models.Productos

class Catalogo (private var IdCatalogo: String, private var NombreCatalogo: String,  private var DescripcionCatalogo: String, private var DisponibilidadCatalogo: Boolean, private var EstiloCatalogo: String){

    fun Catalogo(idCatalogo: String, nombreCatalogo: String, descripcionCatalogo: String, disponibilidadCatalogo: Boolean, estiloCatalogo: String){
        this.IdCatalogo = idCatalogo
        this.NombreCatalogo = nombreCatalogo
        this.DescripcionCatalogo = descripcionCatalogo
        this.DisponibilidadCatalogo = disponibilidadCatalogo
        this.EstiloCatalogo = estiloCatalogo
    }

    fun getIdCatalogo(): String {
        return IdCatalogo
    }

    fun getNombreCatalogo(): String {
        return NombreCatalogo
    }

    fun getDescripcionCatalogo(): String {
        return DescripcionCatalogo
    }

    fun getDisponibilidadCatalogo(): Boolean {
        return DisponibilidadCatalogo
    }

    fun getEstiloCatalogo(): String {
        return EstiloCatalogo
    }


}