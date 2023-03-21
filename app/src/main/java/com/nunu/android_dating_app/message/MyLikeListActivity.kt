package com.nunu.android_dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.nunu.android_dating_app.R
import com.nunu.android_dating_app.auth.UserDataModel
import com.nunu.android_dating_app.utils.FirebaseAuthUtils
import com.nunu.android_dating_app.utils.FirebaseRef

class MyLikeListActivity : AppCompatActivity() {

    private val TAG = "MyLikeListActivity"

    private val uid = FirebaseAuthUtils.getUid()

    // 내가 좋아요 표시한 사람들의 UID 리스트
    private val likeUserListUid = mutableListOf<String>()

    // 내가 좋아요 표시한 사람들의 리스트
    private val likeUserList = mutableListOf<UserDataModel>()

    // 먼저 listViewAdapter를 선언을 해줘야 함
    lateinit var listViewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)

        // ListView  Adapter 연결
        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        // 전체 유저 데이터 불러오는 함수 호출
        getUserDataList()

        // 내가 좋아요 표시한 사람들 리스트를 불러오는 함수 호출
        getMyLikeList()

    }

    // 내가 좋아요 표시한 사람들 리스트를 불러오는 함수
    private fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children){

                    // 내가 좋아요 한 사람들의 uid가 likeUserList에 들어있음
                    likeUserListUid.add(dataModel.key.toString())

                }
                // 전체 유저 데이터를 불러오는 함수 호출
                getUserDataList()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    // 전체 유저 데이터를 불러오는 함수
    private fun getUserDataList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 반복문을 통해 유저정보 아래에 있는 데이터를 반환
                for (dataModel in dataSnapshot.children){

                    // user 정보를 리스트에 추가
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 내가 좋아요 표시한 UID 리스트 안에 유저의 UID 리스트와 같다면
                    if (likeUserListUid.contains(user?.uid)){

                        // 해당 유저의 정보를 추가함
                        likeUserList.add(user!!)

                    }

                }
                listViewAdapter.notifyDataSetChanged()
                Log.d(TAG, likeUserList.toString())

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