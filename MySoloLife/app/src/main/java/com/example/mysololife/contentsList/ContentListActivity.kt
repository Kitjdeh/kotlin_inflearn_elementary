package com.example.mysololife.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference

    //북마크 담을 리스트
    val bookmarkIdList = mutableListOf<String>()

    lateinit var rvAdapter: ContentsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val rv: RecyclerView = findViewById(R.id.rv)

        val items = ArrayList<ContentModel>()

        //items들의 key 값을 저장할 List 생성
        val itemKeyList = ArrayList<String>()

//        val rvAdapter = ContentsRVAdapter(baseContext, items)

        //itemKeyList도 adapter에 넣어준다.

        rvAdapter = ContentsRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        // Write a message to the database
        val database = Firebase.database

        //넘긴 데이터 받아야함
        val category = intent.getStringExtra("category")

        if (category == "category1") {
            myRef = database.getReference("contents")

        } else if (category == "category2") {
            myRef = database.getReference("contents2")
        }

        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    // data를 contentsmodel로 변형
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    // 키값을 저장
                    itemKeyList.add(dataModel.key.toString())
                }
                //새롭게 adpater refresh
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        myRef.addValueEventListener(postListener)

        // adapter에 담은 list의 값을 recyclerview에 전달

        rv.adapter = rvAdapter

        //격자 구성을 위해선 GridLayoutManager , 그냥 일직선이면 LinearLayoutManager
        rv.layoutManager = GridLayoutManager(this, 2)

        getBookMarkData()
    }

    private fun getBookMarkData() {
        // Read from the database
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children) {
                    bookmarkIdList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()
                Log.d("getBookmarkData", bookmarkIdList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}


//        rvAdapter.itemClick = object : ContentsRVAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                val intent = Intent(this@ContentListActivity, ContentShowMainActivity::class.java)
//                // webview쪽에 url데이터를 props해야함
//                intent.putExtra("url", items[position].webUrl)
//                startActivity(intent)
//            }
//        }


//        val myRef2 = database.getReference("contents2")
//        myRef2.push().setValue(
//            ContentModel(
//                "title6",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1240"
//            )
//        )
//        myRef2.push().setValue(
//            ContentModel(
//                "title7",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1241"
//            )
//        )
//        myRef2.push().setValue(
//            ContentModel(
//                "title8",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1242"
//            )
//        )
//        myRef2.push().setValue(
//            ContentModel(
//                "title9",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1243"
//            )
//        )


//        myRef.push().setValue(
//            ContentModel(
//                "title1",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1235"
//            )
//        )
//        myRef.push().setValue(
//            ContentModel(
//                "title2",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1236"
//            )
//        )
//        myRef.push().setValue(
//            ContentModel(
//                "title3",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1237"
//            )
//        )
//        myRef.push().setValue(
//            ContentModel(
//                "title4",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1238"
//            )
//        )
//        myRef.push().setValue(
//            ContentModel(
//                "title5",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1239"
//            )
//        )
//


//        items.add(
//            ContentModel(
//                "title1",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1235"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title2",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1236"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title3",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1237"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title4",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1238"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title5",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1239"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title6",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1240"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title7",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1241"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title8",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1242"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title9",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1243"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title10",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FNNrYR%2Fbtq64wsW5VN%2FqIaAsfmFtcvh4Bketug9m0%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1244"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title11",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FK917N%2Fbtq64SP5gxj%2FNzsfNAykamW7qv1hdusp1K%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1245"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title12",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEO4sy%2Fbtq69SgK8L3%2FttCUxYHx9aPNebNwkPcI21%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1246"
//            )
//        )
//        items.add(
//            ContentModel(
//                "title13",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbdIKDG%2Fbtq64M96JFa%2FKcJiYgKuwKuP3fIyviXm90%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1247"
//            )
//
//        )
//        items.add(
//            ContentModel(
//                "title14",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1248"
//            )
//
//        )
//        items.add(
//            ContentModel(
//                "title15",
//                "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png",
//                "https://philosopher-chan.tistory.com/1249"
//            )
//
//        )