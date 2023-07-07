package com.example.mysololife.contentsList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BookmarkRVAdapter(
    val context: Context,
    val items: ArrayList<ContentModel>,
    val keyList: ArrayList<String>,
    val bookmarkIdList: MutableList<String>
) :
    RecyclerView.Adapter<BookmarkRVAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    //전체 item들을 가져와서 하나하나 씩 넣는거
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        Log.d("ContentRVAdapter", keyList.toString())
        Log.d("ContentRVAdapter", bookmarkIdList.toString())
        return ViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: BookmarkRVAdapter.ViewHolder,
        position: Int
    ) {
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //넘어온 데이터들을 집어넣기
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 추가된 keylist도 파라미터로 넣어준다.
        fun bindItems(item: ContentModel, key: String) {
            itemView.setOnClickListener {
                //데이터 putextra로 넘기기
                val intent = Intent(context, ContentShowMainActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            //북마크 설정
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if (bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            //북마크가 클릭될 경우
            bookmarkArea.setOnClickListener {
                //북마크가 있을 때
                if (bookmarkIdList.contains(key)) {
                    //boomarkIdList의 list 값도 지워야 한다.
                    bookmarkIdList.remove(key)

                    //FBR에 add
                    FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).removeValue()
                }
                //북마크가 없을 때
                else {
                    FBRef.bookmarkRef.child(FBAuth.getUid()).child(key).setValue(
                        BookmarkModel(true)
                    )
                }
            }
            contentTitle.text = item.title
            //imageViewArea에 imageurl을 넣으면 그림이 load된다.
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}