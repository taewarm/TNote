package com.example.tnote.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.API.RetrofitBuilder
import com.example.tnote.API.WeatherBuilder
import com.example.tnote.DataClass.WeatherData
import com.example.tnote.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue

class WeatherActivity :AppCompatActivity() {
    val Key : String = "f481de76938586ea01a1c0cadecc8b0f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)


        weatherAPI()
    }

    fun weatherAPI(){
        val response = WeatherBuilder.getWeather().getweather("37.4869721910739","126.91012871054268",Key)
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                Log.i("여기",response.body()?.main?.temp.toString())
                var companytemp : TextView
                companytemp = findViewById(R.id.weather_temp)
                var temp = response.body()?.main!!.temp
                temp = temp - 273
                companytemp.setText(Math.round(temp).toString()+"도")

            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("여기Error",t.message.toString())
            }
        })
    }
}