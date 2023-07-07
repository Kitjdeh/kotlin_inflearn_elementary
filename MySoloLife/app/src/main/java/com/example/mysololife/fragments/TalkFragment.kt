package com.example.mysololife.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.mysololife.R
import com.example.mysololife.board.BoardInsideActivity
import com.example.mysololife.board.BoardListVAdapter
import com.example.mysololife.board.BoardModel
import com.example.mysololife.board.BoardWriteActivity
import com.example.mysololife.contentsList.BookmarkRVAdapter
import com.example.mysololife.databinding.FragmentTalkBinding
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TalkFragment : Fragment() {

    private lateinit var binding: FragmentTalkBinding

    private val boardDataList = mutableListOf<BoardModel>()

    //게시글 pk값 받을 리스트
    private val boardKeyList = mutableListOf<String>()

    private val TAG = TalkFragment::class.java.simpleName

    //adapter를 통해 데이터를 전달하니까 선언을 나중에 해야 데이터가 전달된다.
    private lateinit var boardRVAdapter: BoardListVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)
        //adpater에 넣을 파라미터(listview 데이터) 생성
//        val boardList = mutableListOf<BoardModel>()
//        boardList.add(BoardModel("a", "b", "c", "d"))


        //만들어둔 adpater 호출
        boardRVAdapter = BoardListVAdapter(boardDataList)
        // listview의 adapter에 연결
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
//          1.listview에 있는 데이터 title,content 등을 props으로 넘겨서 intent를 다른 액티비티를 만든다.
//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title", boardDataList[position].title)
//            intent.putExtra("content", boardDataList[position].content)
//            intent.putExtra("time", boardDataList[position].time)
//            startActivity(intent)
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)

        }


        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }


        binding.bookmarktap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }

        binding.hometap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.storetap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }

        binding.tiptap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }
        getFBBoardData()
        return binding.root
    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}