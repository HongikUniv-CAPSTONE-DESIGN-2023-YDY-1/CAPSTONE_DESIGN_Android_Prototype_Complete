package com.example.pt_b.ui



import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pt_b.databinding.ActivityResultBinding
import com.example.pt_b.retrofit.IRetrofit
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val BASE_URL = "http://nas.robinjoon.xyz:8080"
    val savedUri = intent.getParcelableExtra<android.net.Uri>("savedUri")
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(IRetrofit::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val savedUri = intent.getParcelableExtra<android.net.Uri>("savedUri")
        if (savedUri == null) {
            Toast.makeText(this, "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show()
        }



        binding.searchCon.setOnClickListener {
            intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("location", "CU")
            startActivity(intent)
        }






    }

    private fun absolutelyPath(contentURI: Uri): String {
        var result: String

        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path ?: ""
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }

        return result
    }

    private fun postImageToServer(body: MultipartBody.Part){
        api.postImage(body).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    Toast.makeText(this@ResultActivity, "검색 성공", Toast.LENGTH_SHORT).show()
                    Log.d("API", "이미지 전송 성공")
                    val result = response.body()

                    val jsonResponse =JSONObject(result)
                    val searchItems =jsonResponse.getJSONObject("response").getJSONArray("searchItems")
                    for (i in 0 until searchItems.length()) {
                        val item = searchItems.getJSONObject(i)
                        val name = item.getString("name")
                        val imgUrl = item.getString("imgUrl")
                        val brand = item.getString("brand")
                        val promotion = item.getString("promotion")
                        val pricePerUnit = item.getInt("pricePerUnit")
                        val pricePerGroup = item.getInt("pricePerGroup")

                        Log.d("API", "상품 이름: $name")
                        Log.d("API", "이미지 URL: $imgUrl")
                        Log.d("API", "브랜드: $brand")
                        Log.d("API", "프로모션: $promotion")
                        Log.d("API", "단위 가격: $pricePerUnit")
                        Log.d("API", "그룹 가격: $pricePerGroup")
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("API", "이미지 전송 실패")
                Log.e( "API", t.toString())
                Toast.makeText(this@ResultActivity, "서버에 사진을 업로드 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }

        })
    }


}