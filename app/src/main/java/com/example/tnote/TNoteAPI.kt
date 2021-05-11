package com.example.tnote

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface TNoteAPI {
    @POST("content")
    fun insertContent(
        @Body id : JsonElement
    ): Call<Void>
}