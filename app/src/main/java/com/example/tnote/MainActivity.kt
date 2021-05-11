package com.example.tnote

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var asd = findViewById<TextView>(R.id.asdasd)
        var ddd = findViewById<LinearLayout>(R.id.btn_main_read)
        var asdd : Long
        //여기서 날아오는 id값으로 날짜별 데이터 찾아서 데이터베이스 userid에 넣고
        asdd = intent.getLongExtra("userId",0)
        asd.text = asdd.toString()
        ddd.setOnClickListener {
            APIStart()
        }

    }
    fun APIStart(){
        //이런식으로 JSON형태로 대충 String 만들어주고 JSON으로 변환시킨다음 넣어버리기 그럼 Body로 쓸수있음
        var JsonTestText = "{name:김태원,content:가나다라마바}"
        var ConvertJson = JsonParser.parseString(JsonTestText)
        val response = RetrofitBuilder.getService().insertContent(ConvertJson)
        response.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("여기","성공")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("여기Error",t.message.toString())
            }
        })
    }
}