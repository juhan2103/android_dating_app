package com.nunu.android_dating_app.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nunu.android_dating_app.R
import com.nunu.android_dating_app.auth.UserDataModel
import com.nunu.android_dating_app.utils.FirebaseAuthUtils
import com.nunu.android_dating_app.utils.FirebaseRef

class MyPageActivity : AppCompatActivity() {

    // TAG 이름 설정
    private val TAG = "MyPageActivity"

    // UID를 불러오는 함수 호출
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 내 정보 불러오기 함수 호출
        getMyData()
    }

    // UID 정보를 참조하여 내 정보 불러오기
    private fun getMyData(){

        val myImage = findViewById<ImageView>(R.id.myImage)
        val myUid = findViewById<TextView>(R.id.myUid)
        val myNickname = findViewById<TextView>(R.id.myNickname)
        val myAge = findViewById<TextView>(R.id.myAge)
        val myCity = findViewById<TextView>(R.id.myCity)
        val myGender = findViewById<TextView>(R.id.myGender)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                myUid.text = data!!.uid
                myNickname.text = data!!.nickname
                myAge.text = data!!.age
                myCity.text = data!!.city
                myGender.text = data!!.gender

                // storage에 있는 이미지를 uid를 참조하여 불러옴
                val storageRef = Firebase.storage.reference.child(data.uid + ".png")
                storageRef.downloadUrl.addOnCompleteListener({task ->
                    if (task.isSuccessful){
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImage)
                    }
                })


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        // userInfoRef에 있는 데이터 불러오기
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}