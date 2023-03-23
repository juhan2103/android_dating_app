package com.nunu.android_dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.nunu.android_dating_app.R
import com.nunu.android_dating_app.auth.UserDataModel
import com.nunu.android_dating_app.message.fcm.NotiModel
import com.nunu.android_dating_app.message.fcm.PushNotification
import com.nunu.android_dating_app.utils.FirebaseAuthUtils
import com.nunu.android_dating_app.utils.FirebaseRef

class MyMsgActivity : AppCompatActivity() {

    private val TAG = "MyMsgActivity"

    lateinit var listViewAdapter : MsgAdapter

    val msgList = mutableListOf<MsgModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_msg)

        val listView = findViewById<ListView>(R.id.msgListView)

        listViewAdapter = MsgAdapter(this, msgList)
        listView.adapter = listViewAdapter

        // 메세지를 받아오는 함수 호출
        getMyMsg()

    }

    // 메세지를 받아오는 함수
    private fun getMyMsg(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 겹쳐서 보이는 오류 없애기 위해 clear 함수 사용
                msgList.clear()

                // 반복문을 통해 유저정보 아래에 있는 데이터를 반환
                for (dataModel in dataSnapshot.children){

                    val msg = dataModel.getValue(MsgModel::class.java)

                    msgList.add(msg!!)
                    Log.d(TAG, msg.toString())

                }
                // 최신 순서대로 보여줌
                msgList.reverse()

                // 데이터베이스의 정보 불러옴
                listViewAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // userInfoRef에 있는 데이터 불러오기
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)


    }
}