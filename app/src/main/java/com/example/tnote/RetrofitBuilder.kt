package com.example.tnote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitBuilder {
    var UrlIp : String ="http://13.124.172.29:1750/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(UrlIp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getService(): TNoteAPI = retrofit.create(TNoteAPI::class.java)
}