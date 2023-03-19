package com.nunu.android_dating_app.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object{

        private lateinit var auth : FirebaseAuth

        // 유저의 id를 받아오는 함수
        fun getUid() : String{

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()

        }
    }
}