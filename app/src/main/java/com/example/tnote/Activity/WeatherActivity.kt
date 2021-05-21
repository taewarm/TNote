package com.example.tnote.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tnote.API.RetrofitBuilder
import com.example.tnote.API.WeatherBuilder
import com.example.tnote.DataClass.WeatherData
import com.example.tnote.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import java.util.jar.Manifest
import kotlin.math.absoluteValue

class WeatherActivity :AppCompatActivity() {
    var Key : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        Key = getString(R.string.weather_app_key) //weatherkey

        var companytemp : TextView
        companytemp = findViewById(R.id.weather_temp)
        SyncweatherAPI("37.4869721910739","126.91012871054268")
//        var asdasd = weatherAPI("37.4869721910739","126.91012871054268")
//        Log.i("여기",asdasd.toString())
        //companytemp.setText(asdasd.toString()+"도")
    }

    /**동기처리방식 생각해서 넣을것*/
    fun SyncweatherAPI(lat:String, lon:String) {
        val response = WeatherBuilder.getWeather().getweather(lat,lon,Key)
        var call : WeatherData? = response.execute().body()

    }

    /**비동기처리방식*/
    fun weatherAPI(lat:String, lon:String) : Int{
        val response = WeatherBuilder.getWeather().getweather(lat,lon,Key)
        var temp : Double = 0.0
        var asd : Int = 0
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
//                    temp = response.body()?.main!!.temp
//                    temp = temp - 273.15
//                    asd = temp.toInt()
//                    Log.i("여기",asd.toString())
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("여기Error",t.message.toString())
            }
        })
        return asd
    }
}