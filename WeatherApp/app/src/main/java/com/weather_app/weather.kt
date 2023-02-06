package com.weather_app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class weather : Fragment()
{
    private val start_lon = 21.01178
    private val start_lat = 52.22977

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("DiscouragedApi", "SetTextI18n", "MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        val button = view.findViewById<Button>(R.id.button)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val countryText = view.findViewById<TextView>(R.id.textCountry)
        val cityText = view.findViewById<TextView>(R.id.textCity)
        val weatherText = view.findViewById<TextView>(R.id.textWeather)
        val currentTempText = view.findViewById<TextView>(R.id.textView)
        val textPerceptibleTemperature = view.findViewById<TextView>(R.id.textPerceptibleTemperature)
        val humidityText = view.findViewById<TextView>(R.id.textHumidity)
        val pressureText = view.findViewById<TextView>(R.id.textPressure)
        val speeedText = view.findViewById<TextView>(R.id.speedText)
        val degText = view.findViewById<TextView>(R.id.degText)
        val visibilityText = view.findViewById<TextView>(R.id.textVisibility)
        val cloudsText = view.findViewById<TextView>(R.id.textClouds)
        val sunriseText = view.findViewById<TextView>(R.id.textSunrise)
        val sunsetText = view.findViewById<TextView>(R.id.textSunset)

        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.cities)
        }

        GlobalScope.launch(Dispatchers.Main)
        {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(PlaceholderApi::class.java)
            val data = withContext(Dispatchers.IO)
            {
                val response = async {
                    if(arguments?.getString("city") != null)
                    {
                        val lon = arguments?.getDouble("lon")
                        val lat = arguments?.getDouble("lat")
                        api.weather(lon.toString(), lat.toString())
                    }
                    else api.weather(start_lon.toString(), start_lat.toString())
                }
                return@withContext response.await()
            }

            val call = data.await()

            val con: Context = imageView.context
            val id = con.resources.getIdentifier("_${call.weather[0].icon}",
                                            "drawable", con.packageName)
            imageView.setImageResource(id)

            countryText.text = call.sys.country
            cityText.text = call.name
            weatherText.text = "Weather ${call.weather[0].description}"
            currentTempText.text = "Current Temperature ${call.main.temp} ℃"
            textPerceptibleTemperature.text = "Perceptible Temperature ${call.main.feels_like} ℃"
            humidityText.text = "Humidity ${call.main.humidity} %"
            pressureText.text = "Pressure ${call.main.pressure} hPa"
            speeedText.text = "Speed ${call.wind.speed} meter/sec"
            degText.text = "Degs ${call.wind.deg} °"
            visibilityText.text = "Visibility ${call.visibility} meters"
            cloudsText.text = "Cloudiness ${call.clouds.all} %"
            sunriseText.text = "Sun Rise ${java.time.format.DateTimeFormatter.ISO_INSTANT
                                .format(java.time.Instant.ofEpochSecond(call.sys.sunrise.toLong()))}"
            sunsetText.text = "Sun Set ${java.time.format.DateTimeFormatter.ISO_INSTANT
                                .format(java.time.Instant.ofEpochSecond(call.sys.sunset.toLong()))}"
        }
        return view
    }
}
