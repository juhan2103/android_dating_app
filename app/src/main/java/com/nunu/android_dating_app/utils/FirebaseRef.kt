package com.nunu.android_dating_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object{

        val database = Firebase.database

        // 유저 정보 데이터베이스
        val userInfoRef = database.getReference("userInfo")
    }
}