package com.example.tnote.Activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.tnote.API.WeatherBuilder
import com.example.tnote.DataClass.WeatherData
import com.example.tnote.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.jar.Manifest

/**아이콘 추가해야됨
 * http://openweathermap.org/img/wn/01d.png
 * 저런식으로 뒤에 아이콘 이름 넣어주면 이미지 불러옴 ex)01d*/
class WeatherActivity :AppCompatActivity() {
    var Key : String = ""
    var lat : Double = 0.0
    var lon : Double = 0.0


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        Key = getString(R.string.weather_app_key) //weatherkey

        val Tvlat : TextView = findViewById(R.id.lat)
        val Tvlon : TextView = findViewById(R.id.lon)
        var companytemp : TextView
        companytemp = findViewById(R.id.weather_temp)
        //SyncweatherAPI("37.4869721910739","126.91012871054268")
//        var asdasd = weatherAPI("37.4869721910739","126.91012871054268")
//        Log.i("여기",asdasd.toString())
        //companytemp.setText(asdasd.toString()+"도")
        gpsfunction(Tvlat,Tvlon)
    }

    /**동기처리방식 생각해서 넣을것*/
    fun SyncweatherAPI(lat:String, lon:String) {
        val response = WeatherBuilder.getWeather().getweather(lat,lon,Key)
//        var call : WeatherData? = response.execute().body()

    }

    /**비동기처리방식*/
    fun weatherAPI(lat:String, lon:String) : Int{
        val response = WeatherBuilder.getWeather().getweather(lat,lon,Key)
        var temp : Double = 0.0
        var asd : Int = 0
        val TvTemp : TextView = findViewById(R.id.weather_temp)
        val TvState : TextView = findViewById(R.id.weather_state)
        val TvDes_State : TextView = findViewById(R.id.weather_des_state)
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                temp = response.body()?.main!!.temp
                var State : String = response.body()?.weather?.get(0)?.main.toString()
                var desState : String = response.body()?.weather?.get(0)?.description.toString()
                var icon : String = response.body()?.weather?.get(0)?.icon.toString()
                temp = temp - 273.15
                asd = temp.toInt()
                Log.i("여기",response.body()?.weather.toString())
                TvTemp.setText(asd.toString())
                TvState.setText(State)
                TvDes_State.setText(desState)
                getIconAPI(icon)
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.i("여기",t.message!!)
            }
        })
        return asd
    }
    fun gpsfunction(a:TextView,b:TextView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
            Log.i("여기","퍼미션체크")
        }else{
            Log.i("여기","여긴안해도됨")
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"GPS 위치를 켜주세요",Toast.LENGTH_SHORT).show()
        }else{
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                a.setText(it.result.latitude.toString())
                b.setText(it.result.longitude.toString())
                weatherAPI(it.result.latitude.toString(),it.result.longitude.toString())
            }
            fusedLocationProviderClient.lastLocation.addOnFailureListener {
                Log.i("여기1",it.toString())
            }
            fusedLocationProviderClient.lastLocation.addOnCanceledListener {
                Log.i("여기","성공했다면")
            }
        }
    }
    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { //Can add more as per requirement
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }

    fun getIconAPI(Icon:String){
        val icon : ImageView = findViewById(R.id.weather_icon)
        var iconUrl : String = "http://openweathermap.org/img/wn/"+Icon+".png"
        Glide.with(this).load(iconUrl).into(icon)
    }

}