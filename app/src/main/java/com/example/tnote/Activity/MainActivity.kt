package com.example.tnote.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.R

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_read = findViewById<LinearLayout>(R.id.btn_main_read)
        val btn_write = findViewById<LinearLayout>(R.id.btn_main_write)
        var userID : Long
        //여기서 날아오는 id값으로 날짜별 데이터 찾아서 데이터베이스 userid에 넣고
        userID = intent.getLongExtra("userId",0)
        Log.i("여기",userID.toString())
        btn_read.setOnClickListener {
            val intent = Intent(this, ReadActivity::class.java)
            intent.getLongExtra("userId",0)
            intent.putExtra("userId",userID)
            startActivity(intent)
        }
        btn_write.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }
    }
}