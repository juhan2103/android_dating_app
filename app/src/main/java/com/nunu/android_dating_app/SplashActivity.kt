package com.nunu.android_dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nunu.android_dating_app.auth.IntroActivity
import com.nunu.android_dating_app.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FirebaseAuthUtils 패키지에서 사용자 id를 가져오는 함수를 변수에 저장
        val uid = FirebaseAuthUtils.getUid()

        // uid가 null이면 IntroActivity로 전환
        if(uid == "null"){

            Log.d(TAG, "null")
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            },3000)

        }
        // uid가 null이 아니면 MainActivity로 전환
        else{

            Log.d(TAG, "not null")
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            },3000)

        }

    }
}