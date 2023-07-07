package com.example.mysololife.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.mysololife.R
import com.example.mysololife.auth.IntroActivity
import com.example.mysololife.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        auth = Firebase.auth
//        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val logoutBtn: Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, IntroActivity::class.java)
            //로그아웃하고 이동하면서 그간의 활동 기록 다 날려버리기
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}