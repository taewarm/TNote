package com.example.tnote

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var asd = findViewById<TextView>(R.id.asdasd)
        var asdd : Long
        //여기서 날아오는 id값으로 날짜별 데이터 찾아서 데이터베이스 userid에 넣고
        asdd = intent.getLongExtra("userId",0)
        asd.text = asdd.toString()
    }
}