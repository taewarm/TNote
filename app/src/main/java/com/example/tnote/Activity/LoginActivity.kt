package com.example.tnote.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.tnote.R
import com.example.tnote.API.RetrofitBuilder
import com.google.gson.JsonParser
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val TAG = "Login"
    var id :Long? = 0
    var name : String? =""
    var email : String? =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
        var btn_login = findViewById<LinearLayout>(R.id.btn_login)
        btn_login.setOnClickListener {

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
            }
        }
    fun APIStart(userID:String,name:String,email:String?){
        //이런식으로 JSON형태로 대충 String 만들어주고 JSON으로 변환시킨다음 넣어버리기 그럼 Body로 쓸수있음
        var JsonTestText = "{userID:"+userID+",name:"+name+",email:"+email+"}"
        var ConvertJson = JsonParser.parseString(JsonTestText)
        val response = RetrofitBuilder.getService().insertUser(ConvertJson)
        response.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("여기","성공")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("여기Error",t.message.toString())
            }
        })
    }
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            //Login Fail
            Log.i(TAG,"로그인실패")
        }
        else if (token != null) {
            //Login Success
            Log.i(TAG,"로그인성공")
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    Log.i(TAG, "사용자 정보 요청 성공")
                    val intent = Intent(this, MainActivity::class.java)
                    id = user.id
                    intent.putExtra("userId",id)
                    //nickname null 체크
                    if(user.kakaoAccount?.profile?.nickname != null){
                        name=user.kakaoAccount?.profile?.nickname!!
                        Log.i(TAG,user.kakaoAccount?.profile?.nickname!!)
//                            Log.i(TAG,user.kakaoAccount?.profile?.thumbnailImageUrl!!)
                    }else{
                        name = null
                    }
                    //email null 체크
                    if(user.kakaoAccount?.email != null){
                        Log.i(TAG,user.kakaoAccount?.email!!)
                        email = user.kakaoAccount?.email!!
                    }else{
                        email=null
                    }
                    //연령대 null 체크
                    if(user.kakaoAccount?.ageRange?.name != null){
                        Log.i(TAG,user.kakaoAccount?.ageRange?.name.toString()!!)
                    }
                    //생일 null 체크
                    if(user.kakaoAccount?.birthday != null){
                        Log.i(TAG,user.kakaoAccount?.birthday!!)
                    }
                    APIStart(id.toString(),name!!,email)
                    Toast.makeText(this,"${user.kakaoAccount?.profile?.nickname}님 로그인",Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }
            }
        }
    }
    }
