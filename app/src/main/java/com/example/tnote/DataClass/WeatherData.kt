package com.example.tnote.DataClass

data class WeatherData(
    var current : WeatherCurrentData,
    var hourly : List<WeatherCurrentData>
)
