package com.example.tnote.API

import com.example.tnote.DataClass.ContentData
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface TNoteAPI {
    @POST("user")
    fun insertUser(
        @Body id : JsonElement
    ): Call<Void>

    @GET("tae/UserID={id}&DlvDt={dlvdt}")
    fun searchContent(
        @Path("id") id :String,
        @Path("dlvdt") dlvdt : String
    ): Call<List<ContentData>>

    @POST("content")
    fun insertContent(
        @Body id : JsonElement
    ): Call<Void>
}