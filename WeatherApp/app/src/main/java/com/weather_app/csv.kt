package com.weather_app

import java.io.InputStream

data class City(
    val city: String,
    val lon: Double,
    val lat: Double,
)

fun readCsv(inputStream: InputStream): List<City> {
    val reader = inputStream.bufferedReader()
    val header = reader.readLine()
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            val (city, lon, lat) = it.split(';', ignoreCase = false, limit = 3)
            City(city.trim(), lon.trim().toDouble(), lat.trim().toDouble())
        }.toList()
}
