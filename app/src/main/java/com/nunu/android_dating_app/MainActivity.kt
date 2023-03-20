package com.nunu.android_dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.nunu.android_dating_app.auth.IntroActivity
import com.nunu.android_dating_app.auth.UserDataModel
import com.nunu.android_dating_app.setting.MyPageActivity
import com.nunu.android_dating_app.setting.SettingActivity
import com.nunu.android_dating_app.slider.CardStackAdapter
import com.nunu.android_dating_app.utils.FirebaseRef
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdpater : CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    // Log에 사용할 TAG
    private val TAG = "MainActivity"

    // user의 데이터를 받아놓을 리스트
    private val usersDataList = mutableListOf<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val setting = findViewById<ImageView>(R.id.settingIcon)

        // Icon을 눌렀을 때
        setting.setOnClickListener {

            // SettingActivity로 이동
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        cardStackAdpater = CardStackAdapter(baseContext,usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdpater

        // 회원정보 불러오는 함수 호출
        getUserDataList()

    }

    // 회원정보 받아오는 함수
    private fun getUserDataList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 반복문을 통해 유저정보 아래에 있는 데이터를 반환
                for (dataModel in dataSnapshot.children){
                    Log.d(TAG, dataModel.toString())

                    // user 정보를 리스트에 추가
                    val user = dataModel.getValue(UserDataModel::class.java)
                    usersDataList.add(user!!)
                }

                // 정보 최신화
                cardStackAdpater.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // userInfoRef에 있는 데이터 불러오기
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

}