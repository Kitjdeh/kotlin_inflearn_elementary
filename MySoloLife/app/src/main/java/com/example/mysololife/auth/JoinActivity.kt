package com.example.mysololife.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mysololife.MainActivity
import com.example.mysololife.R
import com.example.mysololife.databinding.ActivityIntroBinding
import com.example.mysololife.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    //firebase 선언
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        //databinding으로 join layout 연동
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            var isGoToJoin = true
            val email = binding.emailArea.text.toString()
            val passwrod1 = binding.passwordArea1.text.toString()
            val passwrod2 = binding.passwrodArea2.text.toString()

            // 값이 비어있는지 확인
            if (email.isEmpty()) {
                isGoToJoin = false
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            if (passwrod1.isEmpty()) {
                isGoToJoin = false
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            }
            if (passwrod2.isEmpty()) {
                isGoToJoin = false
                Toast.makeText(this, "비밀번호확인을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            // 비밀번호1,2 가 동일한지 확인하기
            if (!passwrod1.equals(passwrod2)) {
                isGoToJoin = false
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_LONG).show()
            }
            //비밀번호가 6자리 이상인지
            if (passwrod1.length < 6) {
                isGoToJoin = false
                Toast.makeText(this, "6자리 이상 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            }
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, passwrod1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("성공", "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "회원가입 성공",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val user = auth.currentUser

                            //성공했으니 main페이지로 이동
                            val intent = Intent(this, MainActivity::class.java)

                            //회원가입 후 뒤로가면 앱이 꺼지게 해서 중복 회원가입 방지
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("실패", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "회원가입 실패",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }


    }
}