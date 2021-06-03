package com.example.tnote.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.R

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_TNote = findViewById<LinearLayout>(R.id.btn_main_tnote)
        val btn_Weather = findViewById<LinearLayout>(R.id.btn_main_weather)
        val btn_Music = findViewById<LinearLayout>(R.id.btn_main_music)
        var userID : Long

        //여기서 날아오는 id값으로 날짜별 데이터 찾아서 데이터베이스 userid에 넣고
        userID = intent.getLongExtra("userId",0)
        btn_TNote.setOnClickListener {
                val intent = Intent(this, TNoteActivity::class.java)
                intent.putExtra("userId",userID)
                startActivity(intent)
            }
        btn_Weather.setOnClickListener {
//            Toast.makeText(this,"추후개발",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
        btn_Music.setOnClickListener {
            val intent = Intent(this, MusicActivity::class.java)
            startActivity(intent)
        }
    }
}