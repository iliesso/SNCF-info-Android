package com.example.sncf_app_jaziri

import android.os.Parcel
import android.os.Parcelable

class Station (private val uid: String,
               private val libelle: String,
               private val long: Double,
               private val lat: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble()!!,
        parcel.readDouble()!!
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uid)
        parcel.writeString(libelle)
        parcel.writeDouble(long)
        parcel.writeDouble(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Station> {
        override fun createFromParcel(parcel: Parcel): Station {
            return Station(parcel)
        }

        override fun newArray(size: Int): Array<Station?> {
            return arrayOfNulls(size)
        }
    }
}