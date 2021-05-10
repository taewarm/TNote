package com.example.tnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    val TAG = "Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
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
                        val intent = Intent(this,MainActivity::class.java)
                        val id = user.id
                        intent.putExtra("userId",id)
                        //nickname null 체크
                        if(user.kakaoAccount?.profile?.nickname != null){
                            Log.i(TAG,user.kakaoAccount?.profile?.nickname!!)
//                            Log.i(TAG,user.kakaoAccount?.profile?.thumbnailImageUrl!!)
                        }
                        //email null 체크
                        if(user.kakaoAccount?.email != null){
                            Log.i(TAG,user.kakaoAccount?.email!!)
                        }
                        //연령대 null 체크
                        if(user.kakaoAccount?.ageRange?.name != null){
                            Log.i(TAG,user.kakaoAccount?.ageRange?.name.toString()!!)
                        }
                        //생일 null 체크
                        if(user.kakaoAccount?.birthday != null){
                            Log.i(TAG,user.kakaoAccount?.birthday!!)
                        }
                        startActivity(intent)
                    }
                }

            }
        }
        var btn_login = findViewById<LinearLayout>(R.id.btn_login)
        btn_login.setOnClickListener {

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
            }
        }
    }
