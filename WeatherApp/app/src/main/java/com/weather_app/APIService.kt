package com.weather_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private val key_api = "3d5f88365cd2dc55cd27c66f6ba416b4"
private val metric = "metric"

interface PlaceholderApi
{
    @GET("/data/2.5/weather")
    fun weather(@Query("lon") lot: String,
                @Query("lat") lat: String,
                @Query("appid") key: String = key_api,
                @Query("units") units: String = metric): Call<json>
}