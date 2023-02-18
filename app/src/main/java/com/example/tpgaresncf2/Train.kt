package com.example.sncf_app_jaziri

import android.os.Parcel
import android.os.Parcelable

class Train (private val num: Int,
             private val type: TypeTrain,
             private val localHour: Int,
             private val localMinute: Int): Parcelable {

    var from: Stop? = null;
    var to: Stop? = null;
    var stops: ArrayList<Stop> = arrayListOf()

    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        TypeTrain.valueOf(parcel.readString()!!),
        parcel.readInt()!!,
        parcel.readInt()!!
    ) {
        from = parcel.readParcelable(Stop::class.java.classLoader)!!
        to = parcel.readParcelable(Stop::class.java.classLoader)!!
        parcel.readList(stops, Stop::class.java.classLoader)
    }

    fun getNum(): Int{
        return num
    }

    fun getType(): TypeTrain{
        return type
    }

    fun getLocalHour(): Int{
        return localHour
    }

    fun getLocalMinute(): Int{
        return localMinute
    }

    fun addStop(stop: Stop, departureStation: Boolean, arrivalStation: Boolean){
        //add a stop
    }


    override fun toString(): String {
        return "$localHour:$localMinute - ${this.to?.getStation()?.getLibelle()} \n" +
                "$type $num"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(num)
        if (type != null) parcel.writeString(type.name)
        parcel.writeInt(localHour)
        parcel.writeInt(localMinute)
        parcel.writeParcelable(from, flags)
        parcel.writeParcelable(to, flags)
        parcel.writeList(stops)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Train> {
        override fun createFromParcel(parcel: Parcel): Train {
            return Train(parcel)
        }

        override fun newArray(size: Int): Array<Train?> {
            return arrayOfNulls(size)
        }
    }

}
