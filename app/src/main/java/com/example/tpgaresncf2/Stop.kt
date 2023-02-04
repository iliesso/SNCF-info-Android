package com.example.tpgaresncf2

class Stop(private val station: Station, private val hourArrival: String, private val minuteArrival: String, private val hourDeparture: String, private val minuteDeparture: String) {

    fun getStation(): Station{
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


}