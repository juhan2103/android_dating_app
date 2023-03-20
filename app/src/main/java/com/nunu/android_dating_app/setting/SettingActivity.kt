package com.nunu.android_dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nunu.android_dating_app.R
import com.nunu.android_dating_app.auth.IntroActivity

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val myBtn = findViewById<Button>(R.id.myPageBtn)

        // 마이페이지 버튼을 눌렀을 때
        myBtn.setOnClickListener {
            // MyPageActivity로 이동
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)

        // 로그아웃 버튼을 눌렀을 때
        logoutBtn.setOnClickListener {


            val auth = Firebase.auth
            // 로그아웃을 시켜줌
            auth.signOut()

            // IntroActivity로 전환
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }
}