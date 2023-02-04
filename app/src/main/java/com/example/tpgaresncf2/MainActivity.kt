package com.example.tpgaresncf2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import okhttp3.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val stationsList = ArrayList<Station>()
    val trainsList = ArrayList<Train>()
    val cities = ArrayList<String>()
    val trains = ArrayList<String>()
    val client = OkHttpClient()

    //*******
    //Mise en place des données et des fonctions selon les actions de l'utilisateur.
    //*******

    //Fonction onCreate(): équivalent du main.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Mise en place de l'affichage des trains dans le ListView
        val lvTrains: ListView= findViewById(R.id.lvTrains)
        val adapterTrains = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, trains)
        lvTrains.setAdapter(adapterTrains)

        //Appel de la fonction initListStations() qui prépare les stations en fonctions des gares du fichier gares.csv.
        initListStations()

        //Liaison entre la liste de stations créée et l'AutoCompleteTextView
        val actvStation = findViewById<AutoCompleteTextView>(R.id.actvGare)
        val adapterStation = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        actvStation.setAdapter(adapterStation)

        //Choix de la station dans l'AutoCompleteTextView
        actvStation.setOnItemClickListener{
                parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position)
            stationsList.forEach{
                    if (it.getLibelle() == selectedItem){    //Chercher la station ayant le nom sélectionné.
                        getTrainsApi(it, adapterTrains)     //Rechercher les trains correspondant à la station.
                    }
                }
            }

    }


    //*******
    //Lecture du fichier gares.csv et création d'une liste de stations de train.
    //*******

    fun initListStations(){
        val inputStream = resources.openRawResource(R.raw.gares)
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach {

                //Séparation des valeurs du fichier gares.csv.
                val stationDetails = it.split(';')

                // Création d’une station que l’on ajoute à la liste stations
                stationsList.add(Station(stationDetails[0], stationDetails[1], stationDetails[2].toDouble(), stationDetails[3].toDouble()))
                cities.add(stationDetails[1])
            }
        }
    }


    //*******
    //Récupération des prochains départs pour la station sélectionnée dans l'AutoCompleteTextView.
    //*******

    fun getTrainsApi(station: Station, adapter: ArrayAdapter<*>){
        val url = "https://api.sncf.com/v1/coverage/sncf/stop_areas/stop_area:SNCF:${station.getUid().substring(2)}/departures/?count=8&key=fd590875-e7cb-47ab-a4de-1e8749b075bf"
        val request = Request.Builder().url(url).build()
        println("Requête: $request")
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("\n\n\n\n ERROR d'appel a l'api \n\n\n\n")
            }
            override fun onResponse(call: Call, response: Response) {
                println("Reussite de la requête")
                showTrains((JSONTokener(response.body?.string()).nextValue() as JSONObject), station)

                //Mise à jour de la vue: affichage des trains dans le ListView.
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    adapter.notifyDataSetChanged()      //Ici, 'adapter' correspond à adapterTrains.
                })
            }
        })
    }

    fun showTrains(reponse: JSONObject, station: Station){
        val trainList = ArrayList<Train>()
        val jsonDeparts = reponse.getJSONArray("departures")
        var _trainToStation = station

        //Pour chaque train, récupérer les données nécessaires pour créer un objet Train.
        for (i in 0 until jsonDeparts.length() ){

            var _trainTrajet = jsonDeparts.getJSONObject(i)
            var _trainInformations = _trainTrajet.getJSONObject("display_informations")
            var _trainNum = _trainInformations.getString("trip_short_name").toInt()
            var _trainType = TypeTrain.NULL
            when (_trainInformations.getString("physical_mode")){      //when correspond à un switch
                "TER / Intercités"-> _trainType = TypeTrain.TER
                "Train grande vitesse"-> _trainType = TypeTrain.TGV
                "Intercités"-> _trainType = TypeTrain.Intercites
                "OUIGO"-> _trainType = TypeTrain.OUIGO
            }

            //Attribuer l'objet Station correspondant à la station de départ
            var _trainFromStation: Station = station
            stationsList.forEach{
                if (it.getLibelle().equals(_trainTrajet.getJSONObject("stop_point").getString("name"))){    //Chercher la station ayant le nom sélectionné.
                    var _trainFromStation = it     //Rechercher les trains correspondant à la station.
                }
            }

            stationsList.forEach{
                if (it.getLibelle().equals(_trainTrajet.getJSONObject("route").getJSONObject("direction").getJSONObject("stop_area").getString("name"))) {    //Chercher la station ayant le nom sélectionné.
                    _trainToStation = it     //Rechercher les trains correspondant à la station.
                }
            }

            var _trainFromHour =  ""+(_trainTrajet.getJSONObject("stop_date_time").getString("departure_date_time"))[9] + (_trainTrajet.getJSONObject("stop_date_time").getString("departure_date_time"))[10]
            var _trainFromMinute =  ""+(_trainTrajet.getJSONObject("stop_date_time").getString("departure_date_time"))[11] + (_trainTrajet.getJSONObject("stop_date_time").getString("departure_date_time"))[12]
            var _trainToHour = ""+(_trainTrajet.getJSONObject("stop_date_time").getString("arrival_date_time"))[9] + (_trainTrajet.getJSONObject("stop_date_time").getString("arrival_date_time"))[10]
            var _trainToMinute = ""+(_trainTrajet.getJSONObject("stop_date_time").getString("arrival_date_time"))[11] + (_trainTrajet.getJSONObject("stop_date_time").getString("arrival_date_time"))[12]
            var _trainFrom = Stop(_trainFromStation, _trainFromHour, _trainFromMinute, _trainToHour, _trainToMinute)
            var _trainTo = Stop(_trainToStation, _trainFromHour, _trainFromMinute, _trainToHour, _trainToMinute)

            //Ajout du train à la liste.
            trains.add(Train(_trainNum, _trainType, _trainFrom, _trainTo, _trainFromHour.toInt(), _trainFromMinute.toInt()).toString())
            println("ajout du train $i en direction de ${_trainToStation.getLibelle()}" 
        }
    }



}