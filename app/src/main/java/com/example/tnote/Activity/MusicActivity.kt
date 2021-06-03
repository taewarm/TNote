package com.example.tnote.Activity

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tnote.R

class MusicActivity : AppCompatActivity() {
    var playy : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        val btn_play = findViewById<Button>(R.id.play)
        val sek_playtime = findViewById<SeekBar>(R.id.play_time)
        val txt_playtime = findViewById<TextView>(R.id.play_time_text)
        var bol = true
        Thread(Runnable {
            while (bol){
                playy += 1
                sek_playtime.setProgress(playy)
                Thread.sleep(1000)
                if(playy == 199){
                    bol = false
                }
            }
        }).start()


       var musicUri = Uri.parse("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/music.mp3")
        val musicp = MediaPlayer.create(this,musicUri)
        Log.i("여기",(musicp.duration/1000).toString())
        sek_playtime.max = (musicp.duration/1000)
        musicp.start()

        sek_playtime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                Log.i("여기",progress.toString())
                var min =0
                var sec : String
                when(progress){
                    in 0..59 -> {
                        playy = progress
                        if(progress<10){
                            sec = "0"+progress
                        }else{
                            sec = progress.toString()
                        }
//                        Log.i("여기",min.toString()+":"+sec)
                        musicp.seekTo(progress)
                        txt_playtime.setText(min.toString()+":"+sec)
                    }
                    in 60..119 -> {
                        playy = progress
                       min =1
                        if(progress-60<10){
                            sec = "0"+(progress-60)
                        }else{
                            sec = (progress-60).toString()
                        }
                        musicp.seekTo(progress)
                        txt_playtime.setText(min.toString()+":"+sec)
                    }
                    in 120..179 -> {
                        playy = progress
                        min =2
                        if(progress-120<10){
                            sec = "0"+(progress-120)
                        }else{
                            sec = (progress-120).toString()
                        }
                        musicp.seekTo(progress)
                        txt_playtime.setText(min.toString()+":"+sec)
                    }
                    else -> {
                        playy = progress
                        min =3
                        if(progress-180<10){
                            sec = "0"+(progress-180)
                        }else{
                            sec = (progress-180).toString()
                        }
                        musicp.seekTo(progress)
                        txt_playtime.setText(min.toString()+":"+sec)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.i("여기","누름")
                musicp.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.i("여기","뗌")
                musicp.start()
            }
        })
    }
}