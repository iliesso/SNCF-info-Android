package com.example.tpgaresncf2

class Train (private val num: Int,
             private val type: TypeTrain,
             private val from: Stop,
             private val to: Stop,
             private val localHour: Int,
             private val localMinute: Int){

    fun getNum(): Int{
        return num
    }

    fun getType(): TypeTrain{
        return type
    }

    fun getFrom(): Stop{
        return from
    }

    fun getTo(): Stop{
        return to
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
        return "$localHour:$localMinute - ${to.getStation().getLibelle()} \n" +
                "$type $num"
    }

}
