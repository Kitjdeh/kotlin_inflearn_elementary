package com.example.mysololife.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.comment.CommentListVAdapter
import com.example.mysololife.comment.CommentModel
import com.example.mysololife.databinding.ActivityBoardInsideBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {


    private val TAG = BoardInsideActivity::class.java.simpleName

    private val isImageUpload = false

    private lateinit var binding: ActivityBoardInsideBinding

    private lateinit var key: String

    private val commentList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter: CommentListVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
//       1.listview에 있는 데이터 title,content 등을 props으로 넘겨서 intent를 다른 액티비티를 만든다.
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()

//        binding.titleArea.text = title
//        binding.contentArea.text = content
//        binding.timeArea.text = time
        binding.boardSettingIcon.setOnClickListener {
            showDialog()

        }
//        2.key 값을 받는 방법
        key = intent.getStringExtra("key").toString()
//        Toast.makeText(this, key, Toast.LENGTH_LONG).show()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)

        }

        commentAdapter = CommentListVAdapter(commentList)
        binding.commentLV.adapter = commentAdapter
        getCommentData(key)
    }

    fun getCommentData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for (Commentdata in dataSnapshot.children) {
                    val item = Commentdata.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key: String) {
        //comment
        // -BoardKey
        //      - Commentkey
        //         - CommentData
        //         - CommentData
        FBRef.commentRef.child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime()

                )
            )
        binding.commentArea.setText("")
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            .setTitle("게시글 수정삭제")
        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정페이지 이동", Toast.LENGTH_LONG).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            //정의된 board를 찾아옴
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                //단일 개체 내부를 받으니 for문 불필요
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                if (dataModel != null) {
                    binding.titleArea.text = dataModel!!.title
                    binding.contentArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    // 글쓴이와 유저가 같은 uid인지 확인하고 버튼 투명 변경
                    val myuid = FBAuth.getUid()
                    val writerUid = dataModel.uid
                    if (myuid == writerUid) {
                        binding.boardSettingIcon.isVisible = true
                        Toast.makeText(baseContext, "같음", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "다름", Toast.LENGTH_SHORT).show()
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        //세팅한 함수를 key값과 함께 넣어준다.
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }

    private fun getImageData(key: String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity`
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener({ task ->
            if (task.isSuccessful) {
//                Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.getImageArea.isVisible = false
//                Toast.makeText(this, "fail", Toast.LENGTH_LONG).show()
            }
        })
    }

//        // Download directly from StorageReference using Glide
//        // (See MyAppGlideModule for Loader registration)
//        Glide.with(this)
//            .load(storageReference)
//            .into(imageView)
//    }

}