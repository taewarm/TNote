package com.example.tnote.Activity

import android.content.pm.PackageManager
import android.graphics.Color
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**아이콘 추가해야됨
 * http://openweathermap.org/img/wn/01d.png
 * 저런식으로 뒤에 아이콘 이름 넣어주면 이미지 불러옴 ex)01d*/
class WeatherActivity :AppCompatActivity() {
    var Key : String = ""
    var days : ArrayList<String> = ArrayList()
    var ondo : ArrayList<Int> = ArrayList()
    lateinit var lineChart : LineChart
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        Key = getString(R.string.weather_app_key) //weatherkey

        val Tvlat : TextView = findViewById(R.id.lat)
        val Tvlon : TextView = findViewById(R.id.lon)
        lineChart = findViewById(R.id.lineChart)
        printChart()

        gpsfunction(Tvlat,Tvlon)
    }

    /**동기처리방식 생각해서 넣을것*/
    fun SyncweatherAPI(lat:String, lon:String) {
        val response = WeatherBuilder.getWeather().getweather(lat,lon,"minutely,daily,alerts",Key,"kr")
//        var call : WeatherData? = response.execute().body()

    }

    /**비동기처리방식*/
    fun weatherAPI(lat:String, lon:String) : Int{
        val response = WeatherBuilder.getWeather().getweather(lat,lon,"minutely,daily,alerts",Key,"kr")
        var temp : Double = 0.0
        var asd : Int = 0
        val TvTemp : TextView = findViewById(R.id.weather_temp)
        val TvState : TextView = findViewById(R.id.weather_state)
        val TvDes_State : TextView = findViewById(R.id.weather_des_state)
        val TvTime : TextView = findViewById(R.id.weather_time)
        response.enqueue(object : Callback<WeatherData>{
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                Log.i("여기",response.body()?.hourly?.size.toString())
                var i : Int? = response.body()?.hourly?.size

                if(i != null){
                    for(i in 0..i-24){
                        var time : Int = response.body()?.hourly?.get(i)?.dt!!
                        var temp : Double = response.body()?.hourly?.get(i)?.temp!!
                        var dateFormat = SimpleDateFormat("HH",Locale.getDefault())
                        var date : String = dateFormat.format(Date(time*1000L))//오후에는 더하지말아야되나봄
                        ondo.add((temp - 273.15).toInt())
                        days.add(date)
                        addEntry((temp - 273.15).toFloat())
                    }
                }

                temp = response.body()?.current?.temp!!
                var dt : Int = response.body()?.current?.dt!!
                val State : String = response.body()?.current?.weather?.get(0)?.main.toString()
                val desState : String = response.body()?.current?.weather?.get(0)?.description.toString()
                val icon : String = response.body()?.current?.weather?.get(0)?.icon.toString()
                var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault())
                var date : String = dateFormat.format(Date(dt*1000L))//오후에는 더하지말아야되나봄
//                var date : String = dateFormat.format(Date((dt+32400)*1000L))//가상머신에서는 더해줘야됨 미국시간인지 좌표때문인지 값 이상해짐
                TvTime.setText(date)
                temp = temp - 273.15
                asd = temp.toInt()
                TvTemp.setText(asd.toString()+"ºC")
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
    fun printChart(){
        val xAxis : XAxis = lineChart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            setDrawGridLines(false)
            granularity = 2f    //x축 간격
//            axisMinimum = 12f  //x축 최소
            valueFormatter = MyXAxisFormaater()
            isGranularityEnabled = true
        }
        lineChart.apply {
            axisRight.isEnabled = false
//            axisLeft.axisMaximum = 50f    //Y축 최대
                legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }

        val lineData = LineData()
        lineChart.data = lineData
    }
    fun addEntry(temp : Float){
        val data: LineData = lineChart.data
        data?.let {
            var set: ILineDataSet? = data.getDataSetByIndex(0)
            if(set == null){
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(Entry(set.entryCount.toFloat(),temp),0)
            data.notifyDataChanged()
            lineChart.apply {
                notifyDataSetChanged()
                moveViewToX(data.entryCount.toFloat())
//                setVisibleXRangeMaximum(4f)
                setPinchZoom(true)
                isDoubleTapToZoomEnabled = false
                description.text = "시간"
                description.textSize = 15f
                setExtraOffsets(8f,16f,8f,16f)
            }
        }
    }
    fun createSet():LineDataSet {
        val set = LineDataSet(null,"날씨")
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = R.color.black
            setCircleColor(R.color.black)
            valueTextSize = 10f
            lineWidth = 2f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = R.color.black
            highLightColor = Color.BLUE
            setDrawValues(true)
        }
        return set
    }
    inner class MyXAxisFormaater : ValueFormatter(){
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }
}