package com.example.mysololife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.databinding.ActivityBoardEditBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key: String

    private lateinit var binding: ActivityBoardEditBinding

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writerUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }

    }

    private fun editBoardData(key: String) {
        FBRef.boardRef.child(key)
            .setValue(
                BoardModel(
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    writerUid,
                    FBAuth.getTime()
                )
            )
        Toast.makeText(this, "수정 완", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                //단일 개체 내부를 받으니 for문 불필요
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                if (dataModel != null) {
                    binding.titleArea.setText(dataModel!!.title)
                    binding.contentArea.setText(dataModel!!.content)
                    writerUid = dataModel.uid
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
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener({ task ->
            if (task.isSuccessful) {
//                Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
//                Toast.makeText(this, "fail", Toast.LENGTH_LONG).show()
            }
        })
    }

}