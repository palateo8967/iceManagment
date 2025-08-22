package com.icerojects.icemanagment.utils

//Clase cerrada
//Data guarda el resultado de la operacion
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    //caso de exito, devulve el dato recinido
    class Success<T>(data: T?) : Resource<T>(data)
    //caso de error, guarda un mensaje de error y el ultimo dato que se pudo cargar
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    //Para cuando una peticion todavia esta cargando
    class Loading<T>(data: T? = null) : Resource<T>(data)

}