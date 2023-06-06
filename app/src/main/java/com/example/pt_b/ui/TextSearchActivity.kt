package com.example.pt_b.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pt_b.databinding.ActivityTextSearchBinding
import com.example.pt_b.recyclerview.ListAdapter
import com.example.pt_b.recyclerview.ListLayout
import com.example.pt_b.retrofit.IRetrofit
import com.example.pt_b.retrofit.ResultSearchKeyword
import com.example.pt_b.retrofit.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextSearchBinding
    private var listItem = ArrayList<ListLayout>() // 리사이클러 뷰 아이템
    private var listAdapter = ListAdapter(listItem) // 리사이클러 뷰 어댑터
    private var strength = "STRONG" // 검색 조건

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러 뷰
        binding.rvSearchItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchItems.adapter = listAdapter

        // 검색 버튼 클릭시
        binding.btnSearch.setOnClickListener {
            val name = binding.etSearchItmes.text.toString()
            searchItems(name, strength)

        }

    }

    private fun searchItems(keyword: String, strength: String) {

        val retrofit = RetrofitClient.getClient("http://nas.robinjoon.xyz:8080") ?: return // 레트로핏 구성
        val api = retrofit.create(IRetrofit::class.java) // IRetrofit을 객체로 생성
        val call = api.searchItems(keyword, strength) // 검색 조건 입력

        // 서버에 요청
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                // 통신 성공
                val result = response.body()
                addItems(result)
                Log.d("API", "API 서버와의 통신 성공")
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@TextSearchActivity, "통신 실패: ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("API", "API 서버와의 통신 실패")
            }
        })
    }

    private fun addItems(searchResult: ResultSearchKeyword?) {
        val response = searchResult?.response
        val searchItems = response?.searchItems

        if (!searchItems.isNullOrEmpty()) {
            listItem.clear()
            for (item in searchItems) {
                val name = item.name ?: ""
                val imgUrl = item.imgUrl ?: ""
                val brand = item.brand ?: ""
                val promotion = item.promotion ?: ""
                val pricePerUnit = item.pricePerUnit ?: 0

                val listItems = ListLayout(name, imgUrl, brand, promotion, pricePerUnit)
                listItem.add(listItems)
            }
            listAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

}


