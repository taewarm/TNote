package com.example.tnote.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherBuilder {
    var WeatherURI : String ="https://api.openweathermap.org/"
    private val weather = Retrofit.Builder()
        .baseUrl(WeatherURI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getWeather(): WeatherAPI = weather.create(WeatherAPI::class.java)
}