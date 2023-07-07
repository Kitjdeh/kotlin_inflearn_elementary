package com.example.sample1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BoardListActivity : AppCompatActivity() {

    // 우선 타입을 정하고 값을 나중에 넣는 선언 (Flutter에서 걍 int A 하던 그런거)
    lateinit var LVAdapter: ListViewAdapter

    val list = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_list)

        val writeBtn = findViewById<Button>(R.id.writeBtn)
        writeBtn.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        LVAdapter = ListViewAdapter(list)
        val lv = findViewById<ListView>(R.id.lv)
        lv.adapter = LVAdapter

        getData()
    }

    fun getData() {
        // 저정할 데이터 베이스와 path 값을 선언
        val database = Firebase.database
        val myRef = database.getReference("board")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d("BoardListActivity11111", dataSnapshot.toString())
                // ...
                //반복문으로 데이터를 빼온다.
                for (datamodel in dataSnapshot.children) {
                    //반복문의 객체 정의
                    val item = datamodel.getValue(DataModel::class.java)
                    Log.d("BoardListActivity11111", item.toString())
                    list.add(item!!)
                }
                //리스트 생성 후 data를 넣는거니까 리액트의 useeffect처럼 관리해야함
                LVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BoardListActivity11111", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
    }
}