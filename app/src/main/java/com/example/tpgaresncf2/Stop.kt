package com.example.sncf_app_jaziri

import android.os.Parcel
import android.os.Parcelable

class Stop(private val station: Station?, private val hourArrival: String, private val minuteArrival: String, private val hourDeparture: String, private val minuteDeparture: String): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Station::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    fun getStation(): Station?{
        return station
    }

    fun getHourArrival(): String{
        return hourArrival
    }

    fun getMinuteArrival(): String{
        return minuteArrival
    }

    fun getHourDeparture(): String{
        return hourDeparture
    }

    fun getMinuteDeparture(): String{
        return minuteDeparture
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(station, flags)
        parcel.writeString(hourArrival)
        parcel.writeString(minuteArrival)
        parcel.writeString(hourDeparture)
        parcel.writeString(minuteDeparture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stop> {
        override fun createFromParcel(parcel: Parcel): Stop {
            return Stop(parcel)
        }

        override fun newArray(size: Int): Array<Stop?> {
            return arrayOfNulls(size)
        }
    }


}

private fun Parcel.readParcelable(java: Class<Station>) {
    //aa
}
