package com.nunu.android_dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nunu.android_dating_app.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // 로그인 화면으로 이동
        val loginBtn : Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener{

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 화면으로 이동
       val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {

            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }

    }
}