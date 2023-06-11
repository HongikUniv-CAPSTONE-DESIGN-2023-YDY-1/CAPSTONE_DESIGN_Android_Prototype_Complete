package com.example.pt_b.ui




import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pt_b.databinding.ActivityResultBinding
import com.example.pt_b.recyclerview.ListAdapter
import com.example.pt_b.recyclerview.ListLayout
import com.example.pt_b.recyclerview.ResultAdapter
import com.example.pt_b.recyclerview.ResultLayouot
import com.example.pt_b.retrofit.IRetrofit
import com.example.pt_b.retrofit.ItemInfo
import com.example.pt_b.retrofit.ResultSearchKeyword
import com.example.pt_b.retrofit.RetrofitClient
import okhttp3.MediaType
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var savedUri: Uri? = null
    private var listItem = ArrayList<ResultLayouot>() // 리사이클러 뷰 아이템
    private var listAdapter = ResultAdapter(listItem) // 리사이클러 뷰 어댑터


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        savedUri = intent.getParcelableExtra<Uri>("savedUri")
        if (savedUri == null) {
            Toast.makeText(this, "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show()
        }
        val imgUri = absolutelyPath(savedUri!!)
        uploadImageToServer(imgUri)
        binding.rvResultItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvResultItems.adapter = listAdapter
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 200
        itemAnimator.removeDuration = 200
        itemAnimator.moveDuration = 200
        itemAnimator.changeDuration = 200
        binding.rvResultItems.itemAnimator = itemAnimator

    }

    private fun absolutelyPath(contentURI: Uri): String {
        var result: String

        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)

        if (cursor == null) {
            result = contentURI.path ?: ""
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        return result
    }

    fun uploadImageToServer(imgUri: String) {
        val file = File(imgUri)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val imagePart = MultipartBody.Part.createFormData("img", file.name, requestBody)
        val retrofit = RetrofitClient.getClient("http://nas.robinjoon.xyz:8080") ?: return
        val api = retrofit.create(IRetrofit::class.java)
        val call = api.postImage(imagePart)
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                // 통신 성공

                val result = response.body()
                addInfo(result)
                Log.d("API", result.toString())
                Log.d("API", "API 서버와의 통신 성공")

            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@ResultActivity, "통신 실패: ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("API", "API 서버와의 통신 실패")
            }
        })


    }
    private fun addInfo(searchResult: ResultSearchKeyword?) {
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
                val pricePerGroup = item.pricePerGroup ?: 0
                val listItems = ResultLayouot(brand, name, pricePerUnit, pricePerGroup, promotion, imgUrl)
                listItem.add(listItems)
            }
            listAdapter.notifyDataSetChanged()
        } else {
            intent = Intent(this, CameraActivity::class.java)
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }


}

