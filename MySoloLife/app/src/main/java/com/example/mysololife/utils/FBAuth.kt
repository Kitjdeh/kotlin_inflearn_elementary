package com.example.mysololife.utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FBAuth {

    companion object {

        private lateinit var auth: FirebaseAuth

        fun getUid(): String {
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun getTime(): String {
            val currentDateTime = Calendar.getInstance().time

            val dateFormat =
                //날짜 표현 방식 및 지역 설정 후 정희한 currentdatetime 입력
                SimpleDateFormat("yyyy.MM.dd. HH:mm:ss", Locale.KOREA)
                    .format(currentDateTime)
            return dateFormat
        }

    }
}