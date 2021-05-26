package com.example.tnote.DataClass

data class WeatherCurrentData(
    var dt : Int,
    var temp : Double,
    var clouds : Int,
    var weather : List<WeatherweatherData>
)
