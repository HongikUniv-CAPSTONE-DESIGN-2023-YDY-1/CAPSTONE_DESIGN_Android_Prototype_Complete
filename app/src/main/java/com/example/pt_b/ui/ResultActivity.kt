package com.example.pt_b.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.pt_b.databinding.ActivityResultBinding
import com.example.pt_b.retrofit.IRetrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val BASE_URL = "http://nas.robinjoon.xyz:8080"
    private lateinit var retrofit: Retrofit
    private lateinit var api: IRetrofit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val savedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("savedUri", android.net.Uri::class.java)
        } else {
            intent.getParcelableExtra<android.net.Uri>("savedUri")
        }
        if (savedUri == null) {
            Toast.makeText(this, "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show()
        }
        //레트로핏
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(IRetrofit::class.java)













        binding.searchCon.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }



    }

    private fun uploadImageToApi(savedUri: android.net.Uri) {
        val Imagefile = File(savedUri.path)
        //멀티파트바디 생성
        val requestBody = Imagefile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", Imagefile.name, requestBody)
        //Api 호출 및 이미지 업로드
        val call = api.postImage(imagePart)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()?.toString()
                    binding.searchImg.setImageURI(result?.toUri())
                    Toast.makeText(this@ResultActivity, "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ResultActivity, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "오류: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "오류: $t")
            }

        })

    }
}