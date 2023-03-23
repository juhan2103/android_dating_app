package com.nunu.android_dating_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object{

        val database = Firebase.database

        // 유저 정보 데이터베이스
        val userInfoRef = database.getReference("userInfo")

        // 유저 좋아요 데이터베이스
        val userLikeRef = database.getReference("userLike")

        // 유저 메세지 데이터베이스
        val userMsgRef = database.getReference("userMsg")
    }
}