package com.example.tnote.Activity

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.API.RetrofitBuilder
import com.example.tnote.DataClass.ContentData
import com.example.tnote.R
import com.google.gson.JsonParser
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)


        var txt_dlvdt = findViewById<TextView>(R.id.write_txt_dlvdt)
        var wrt_edt_title = findViewById<EditText>(R.id.write_edt_title)
        var wrt_edt_content = findViewById<EditText>(R.id.write_edt_content)
        var scrollview = findViewById<ScrollView>(R.id.write_scv)
        var btn_cancel = findViewById<LinearLayout>(R.id.btn_cancel)
        var btn_ok = findViewById<LinearLayout>(R.id.btn_ok)

        scrollview.setOnClickListener {
            wrt_edt_content.requestFocus()
        }
        var UserID : Long

        UserID = intent.getLongExtra("userId",0)

        txt_dlvdt.setText(dlvdtinit())

        val intent = Intent(this, MainActivity::class.java)
        btn_ok.setOnClickListener {
            Log.i("여기","ok")
            var title = wrt_edt_title.text.toString()
            var content = wrt_edt_content.text.toString()
            Log.i("여기",title.length.toString()+","+content.length)
            //APIInsertContent(UserID.toString(),dlvdtinit(),title,content)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
        }
        btn_cancel.setOnClickListener {
            Log.i("여기","cancel")
//            onBackPressed()
            wrt_edt_content.requestFocus()
            var imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }
    fun APIInsertContent(userID:String,dlvdt:String,title:String,content:String){
        //이런식으로 JSON형태로 대충 String 만들어주고 JSON으로 변환시킨다음 넣어버리기 그럼 Body로 쓸수있음
        var JsonTestText = "{userID:"+userID+",DlvDt:"+dlvdt+",title:"+title+",content:"+content+"}"
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
    fun dlvdtinit(): String{
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
        return year+month+day
    }
}