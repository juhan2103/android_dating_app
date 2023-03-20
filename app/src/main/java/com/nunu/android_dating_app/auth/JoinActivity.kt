package com.nunu.android_dating_app.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nunu.android_dating_app.MainActivity
import com.nunu.android_dating_app.R
import com.nunu.android_dating_app.utils.FirebaseRef
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private var TAG = "JoinActivity"

    private lateinit var auth : FirebaseAuth

    // 닉네임 성별 지역 나이 UID
    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""

    // 회원가입 화면 프로필 이미지
    lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Initialize Firebase Auth

        auth = Firebase.auth

        profileImage = findViewById(R.id.imageArea)

        // 갤러리 불러오는 함수
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)
            }
        )

        // 프로필 이미지를 눌렀을 때
        profileImage.setOnClickListener {
            getAction.launch("image/*")
        }


        // joinBtn을 눌렀을 때
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {

            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.pwdArea)

            val emailCheck = email.text.toString()

            // email 입력란이 비어있을때
            if(emailCheck.isEmpty()){
                Toast.makeText(this, "이메일이 입력되지 않았습니다", Toast.LENGTH_LONG).show()
            }

            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()


            // 회원가입으로 유저 생성
            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    // 회원가입이 성공했을 때
                    if (task.isSuccessful) {

                        // uid 받아오기
                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userModel = UserDataModel(
                            uid,
                            nickname,
                            age,
                            gender,
                            city
                        )

                        // 유저 정보를 데이터베이스에 저장
                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                        // 프로필 이미지를 해당 유저의 storage에 저장
                        uploadImage(uid)

                        // MainActivity로 화면 전환
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }
                    // 실패했을 때
                    else {

                    }
                }
        }

    }

    // storage에 이미지를 저장 함수
    private fun uploadImage(uid : String){

        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")

        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}