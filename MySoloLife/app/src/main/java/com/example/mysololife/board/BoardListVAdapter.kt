package com.example.mysololife.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mysololife.R
import com.example.mysololife.utils.FBAuth

class BoardListVAdapter(val boardList: MutableList<BoardModel>) : BaseAdapter() {
    // list 숫자
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
//        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.board_list_item, parent, false)
//        }


        //데이터를 넣을 틀을 가져온다.
        val title = convertView?.findViewById<TextView>(R.id.titleArea)
        val content = convertView?.findViewById<TextView>(R.id.contentArea)
        val time = convertView?.findViewById<TextView>(R.id.timeArea)

        val itemLinearLayoutView = convertView?.findViewById<LinearLayout>(R.id.itemView)
        if (boardList[position].uid.equals(FBAuth.getUid())) {
            itemLinearLayoutView?.setBackgroundColor(Color.parseColor("#ffa500"))
        }

        // 틀에다가 boardList값을 넣는다.

        title!!.text = boardList[position].title
        content!!.text = boardList[position].content
        time!!.text = boardList[position].time
        return convertView!!
    }

}