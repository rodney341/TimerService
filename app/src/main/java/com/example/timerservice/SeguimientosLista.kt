package com.example.timerservice

class SeguimientosLista : RepositorioSeguimiento {

    val listaSeguimiento= mutableListOf<Seguimiento>()

    override fun getById(id: Int): Seguimiento {
        return listaSeguimiento[id]
    }

    override fun save(seguimiento: Seguimiento) {
        listaSeguimiento.add(seguimiento)
    }

    override fun nuevo(): Int {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        return listaSeguimiento.size
    }

    override fun update(id: Int, seguimiento: Seguimiento) {
        TODO("Not yet implemented")
    }


}