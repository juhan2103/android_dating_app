package com.nunu.android_dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nunu.android_dating_app.MainActivity
import com.nunu.android_dating_app.R

class JoinActivity : AppCompatActivity() {

    private var TAG = "JoinActivity"

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Initialize Firebase Auth

        auth = Firebase.auth

        // joinBtn을 눌렀을 때
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)

            // 회원가입으로 유저 생성
            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    // 회원가입이 성공했을 때
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUser")

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }
                    // 실패했을 때
                    else {

                    }
                }
        }

    }
}