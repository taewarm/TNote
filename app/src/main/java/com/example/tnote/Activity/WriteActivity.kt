package com.example.tnote.Activity

import android.hardware.input.InputManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.R

class WriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        var wrt_edt = findViewById<EditText>(R.id.write_edt_content)
        var scrollview = findViewById<ScrollView>(R.id.write_scv)
        var btn_cancel = findViewById<LinearLayout>(R.id.btn_cancel)
        var btn_ok = findViewById<LinearLayout>(R.id.btn_ok)

        btn_ok.setOnClickListener {
            Log.i("여기","ok")
        }
        btn_cancel.setOnClickListener {
            Log.i("여기","cancel")
        }
    }
}