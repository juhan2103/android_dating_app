package com.nunu.android_dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nunu.android_dating_app.auth.IntroActivity
import com.nunu.android_dating_app.auth.UserDataModel
import com.nunu.android_dating_app.setting.MyPageActivity
import com.nunu.android_dating_app.setting.SettingActivity
import com.nunu.android_dating_app.slider.CardStackAdapter
import com.nunu.android_dating_app.utils.FirebaseAuthUtils
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

    private var userCount = 0

    // 현재 유저의 성별 불러오기
    private lateinit var currentUserGender : String

    // UID 불러오기
    private val uid = FirebaseAuthUtils.getUid()

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

            // CardStackView를 Swipe 했을 때
            override fun onCardSwiped(direction: Direction?) {

                // 방향이 오른쪽일 때
                if (direction == Direction.Right){
                    Toast.makeText(this@MainActivity, "right",Toast.LENGTH_SHORT).show()

                    // 좋아요 표시를 한 다른 유저 UID 저장
                    userLikeOtherUser(uid, usersDataList[userCount].uid.toString())
                }

                // 방향이 왼쪽일 때
                if (direction == Direction.Left){
                    Toast.makeText(this@MainActivity, "left",Toast.LENGTH_SHORT).show()
                }

                userCount = userCount + 1

                // 카드를 다 넘기면
                if (userCount == usersDataList.count()){
                    // 다시 유저 정보를 받아옴
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MainActivity, "User Refresh",Toast.LENGTH_LONG).show()
                }

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

        // 나의 성별을 불러오는 함수 호출
        getMyuserData()

    }

    // 나의 성별을 불러오는 함수
    private fun getMyuserData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = dataSnapshot.getValue(UserDataModel::class.java)

                currentUserGender = data?.gender.toString()

                // 회원정보 불러오는 함수 호출
                getUserDataList(currentUserGender)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // userInfoRef에 있는 데이터 불러오기
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    // 회원정보 받아오는 함수
    private fun getUserDataList(currentUserGender : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 반복문을 통해 유저정보 아래에 있는 데이터를 반환
                for (dataModel in dataSnapshot.children){
                    Log.d(TAG, dataModel.toString())

                    // user 정보를 리스트에 추가
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (user!!.gender.toString().equals(currentUserGender)){

                    }
                    // 나의 성별과 다르면 다른 성별의 유저를 리스트에 저장
                    else{

                        usersDataList.add(user!!)

                    }

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

    // 유저의 좋아요를 저장하는 함수
    private fun userLikeOtherUser(myUid : String, otherUid : String){

        // 좋아요 누른 다른 유저 UID를 나의 UID 하위 목록 데이터베이스에 저장
        FirebaseRef.userLikeRef.child(uid).child(otherUid).setValue(true)
    }

}