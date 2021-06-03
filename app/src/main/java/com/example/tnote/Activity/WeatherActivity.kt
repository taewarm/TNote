package com.example.tnote.Activity

import android.content.pm.PackageManager
import android.database.Observable
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
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
//import io.reactivex.rxjava3.*
//import io.reactivex.rxjava3.core.Observable
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
    var check : Int =0
    lateinit var lineChart : LineChart
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var Tvlat : TextView
    lateinit var Tvlon : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        Key = getString(R.string.weather_app_key) //weatherkey

        Tvlat = findViewById(R.id.lat)
        Tvlon = findViewById(R.id.lon)
        check = ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        lineChart = findViewById(R.id.lineChart)
        printChart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
            Log.i("여기","퍼미션체크")
        }else{
            Log.i("여기","여긴안해도됨")
        }
        if(check ==0){
            gpsfunction(Tvlat,Tvlon)
        }else{
            Log.i("여기","지도권한부여")
        }
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
        //원래는 Url주소로 API홈페이지에서 가져오려고했으나 퀄리티가 별로여서 그냥 직접따다 집어넣음
        var image : Int = 0
        when(Icon){
            "01d" -> image = 2131165355
            "01n" -> image = 2131165356
            "02d" -> image = 2131165357
            "02n" -> image = 2131165358
            "03d" -> image = 2131165359
            "03n" -> image = 2131165360
            "04d" -> image = 2131165361
            "04n" -> image = 2131165361
            "09d" -> image = 2131165362
            "09n" -> image = 2131165362
            "10d" -> image = 2131165363
            "10n" -> image = 2131165363
            "11d" -> image = 2131165364
            "11n" -> image = 2131165364
            "13d" -> image = 2131165365
            "13n" -> image = 2131165365
            "50d" -> image = 2131165366
            "50n" -> image = 2131165366
        }
        val icon : ImageView = findViewById(R.id.weather_icon)
        //이건원래 URl 로 받아서 뿌려줬던거 안이쁜이미지
//        var iconUrl : String = "http://openweathermap.org/img/wn/"+Icon+".png"
//        Glide.with(this).load(iconUrl).into(icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val drawable = getDrawable(image)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            icon.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this,"롤리팝이하는안됨",Toast.LENGTH_LONG).show()
        }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        check = ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
        if(check == 0){
            Log.i("여기","설마?")
            gpsfunction(Tvlat,Tvlon)
        }else{
            Toast.makeText(this,"위치에 액세스 권한을 허용하셔야합니다.",Toast.LENGTH_LONG).show()
        }


    }
}