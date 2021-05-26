package com.example.tnote.API

import com.example.tnote.DataClass.ContentData
import com.example.tnote.DataClass.WeatherData
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface WeatherAPI {
//    @POST("user")
//    fun insertUser(
//        @Body id : JsonElement
//    ): Call<Void>

    @GET("data/2.5/onecall")
    fun getweather(
        @Query("lat") lat :String,
        @Query("lon") long : String,
        @Query("exclude") exclude : String,
        @Query("appid") Key : String,
        @Query("lang") lang : String,
    ): Call<WeatherData>
//
//    @POST("content")
//    fun insertContent(
//        @Body id : JsonElement
//    ): Call<Void>
}