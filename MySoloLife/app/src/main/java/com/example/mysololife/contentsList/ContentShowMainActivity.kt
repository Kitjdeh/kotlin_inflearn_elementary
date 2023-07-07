package com.example.mysololife.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.example.mysololife.R

class ContentShowMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show_main)

        //props한 weburl를 받는다.
        val getUrl = intent.getStringExtra("url")
//        Toast.makeText(this, getUrl, Toast.LENGTH_LONG).show()
        //url로 webview를 켜야한다.

        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString())
    }
}