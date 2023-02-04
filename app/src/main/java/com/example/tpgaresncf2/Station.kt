package com.example.tpgaresncf2

class Station (private val uid: String,
             private val libelle: String,
             private val long: Double,
             private val lat: Double){

    fun getUid(): String{
        return uid
    }

    fun getLibelle(): String{
        return libelle
    }

    fun getLong(): Double{
        return long
    }

    fun getLat(): Double{
        return lat
    }

    fun getCoordonnees(): String{
        return "$long , $lat"
    }

    override fun toString(): String {
        return libelle
    }
}