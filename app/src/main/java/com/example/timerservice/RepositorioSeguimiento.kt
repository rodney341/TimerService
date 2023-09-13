package com.example.timerservice

interface RepositorioSeguimiento {

    fun getById(id: Int): Seguimiento  //Devuelve el elemento dado su id
    fun save(seguimiento: Seguimiento)      //Añade el elemento indicado
    fun nuevo(): Int       //Añade un elemento en blanco y devuelve su id
    fun delete(id: Int)    //Elimina el elemento con el id indicado
    fun size(): Int     //Devuelve el número de elementos
    fun update(id: Int, seguimiento: Seguimiento)  //Reemplaza un elemento



}