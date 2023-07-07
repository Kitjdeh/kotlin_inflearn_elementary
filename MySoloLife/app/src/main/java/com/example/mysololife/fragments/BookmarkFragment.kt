package com.example.mysololife.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysololife.R
import com.example.mysololife.contentsList.BookmarkRVAdapter
import com.example.mysololife.contentsList.ContentModel
import com.example.mysololife.databinding.FragmentBookmarkBinding
import com.example.mysololife.utils.FBAuth
import com.example.mysololife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding

    private val TAG = BookmarkFragment::class.java.simpleName

    val bookmarkIdList = mutableListOf<String>()
    val items = ArrayList<ContentModel>()
    val itemsKeyList = ArrayList<String>()

    lateinit var rvAdapter: BookmarkRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)


        //2. 사용자가 북마크한 정보를 다 가져옴!
        getBookmarkData()
        //3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌

        // items, 전체 키 리스트, 북마크 리스트 3가지를 넘기는 adapter를 선언
        rvAdapter = BookmarkRVAdapter(requireContext(), items, itemsKeyList, bookmarkIdList)

        // RecyclerView인 rv의 adapter임을 선언
        val rv: RecyclerView = binding.bookmarkRV
        rv.adapter = rvAdapter

        // RecyclerView에 필요한 layoutmanger
        rv.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.hometap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }
        binding.tiptap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }
        binding.talktap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }
        binding.storetap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }
        return binding.root

    }

    //1. 전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
    private fun getCategotyData() {
        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())
                    var item = dataModel.getValue(ContentModel::class.java)

                    //3. 전체 컨텐츠 중에서, bookmark 값을 가지는지 확인
                    if (bookmarkIdList.contains((dataModel.key.toString()))) {
                        items.add(item!!)
                        itemsKeyList.add(dataModel.key.toString())
                    }

                }
                // 데이터를 다 넣는 비동기 작업 끝났으니 갱신
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        items.clear()
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
    }

    //2. 사용자가 북마크한 정보를 다 가져옴!
    private fun getBookmarkData() {

        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    Log.e(TAG, dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                }
                //1. 전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
                getCategotyData()

                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }
}