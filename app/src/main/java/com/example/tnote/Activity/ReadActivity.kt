package com.example.tnote.Activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.DataClass.ContentData
import com.example.tnote.R
import com.example.tnote.API.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ReadActivity : AppCompatActivity() {
    var UserID : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        UserID = intent.getLongExtra("userId",0)

        val instance = Calendar.getInstance()
        var year = instance.get(Calendar.YEAR).toString()
        var month = (instance.get(Calendar.MONTH)+1).toString()
        var day = instance.get(Calendar.DATE).toString()
        if(month.toInt() < 10){
            month = "0"+month
        }
        if(day.toInt()<10){
            day = "0"+day
        }

//        val now = LocalDateTime.now()
//        if(now.monthValue<10){
//            month = "0"+now.monthValue
//        }else{
//            month = now.monthValue.toString()
//        }

        val dlvdt = findViewById<TextView>(R.id.read_dlvdt)


        dlvdt.setText(year+"년"+month+"월"+day+"일")
        Log.i("여기",UserID.toString())
        APIContent(UserID,year+month+day)
    }

    fun APIContent(userID:Long,DlvDt:String){
        var contentt = findViewById<TextView>(R.id.read_contents)
        val titlee = findViewById<TextView>(R.id.read_title)
        val response = RetrofitBuilder.getService().searchContent(userID.toString(),DlvDt)
        response.enqueue(object : Callback<List<ContentData>> {
            override fun onResponse(call: Call<List<ContentData>>, response: Response<List<ContentData>>) {
                Log.i("여기",response.body().toString())
                var content : List<ContentData>? = response.body()
                if(content?.size != 0){
                    var title = ""
                    var contents = ""
                    if(content?.get(0)?.Content != null){
                        contents = content?.get(0)?.Content!!.replace("%n","\n")
                        Log.i("여기",contents)
//                        contents = content?.get(0)?.Content
                    }
                    if(content?.get(0)?.Title!=null){
                        title = content?.get(0)?.Title
                    }
                    contentt.setText(contents)
                    titlee.setText(title)
                }else{
                    titlee.setText("내용 없음")
                    contentt.setText("내용이 없습니다.")
                }
            }
            override fun onFailure(call: Call<List<ContentData>>, t: Throwable) {
                Log.i("여기Error",t.message.toString())
                Toast.makeText(applicationContext,"서버를 키고 하세요",Toast.LENGTH_LONG).show()
            }
        })
    }
}