package com.example.tnote.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var UrlIp : String ="http://13.124.172.29:1750/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(UrlIp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getService(): TNoteAPI = retrofit.create(TNoteAPI::class.java)
}