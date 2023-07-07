package com.example.sample1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val List: MutableList<DataModel>) : BaseAdapter() {
    // 리스트의 길이
    override fun getCount(): Int {
        return List.count()
    }

    // Int값을 파라미터로 가지며 해당 위치의 item을 말한( 인덱스)
    override fun getItem(position: Int): Any {
        return List[position]
    }

    //헤당 위치의 item id를 반환 (pk 값이 필요할 경우 사용)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // xml 파일의 View와 데이터를 연결하는 핵심열할을 하는 메소드이다.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view =
                LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }
        val title = view?.findViewById<TextView>(R.id.itemTextId)
        title!!.text = List[position].title
        return view!!
    }
}