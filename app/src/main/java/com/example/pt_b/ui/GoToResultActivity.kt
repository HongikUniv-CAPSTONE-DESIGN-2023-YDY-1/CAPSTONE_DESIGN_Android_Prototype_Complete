package com.example.pt_b.ui

import android.media.MediaTimestamp
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pt_b.databinding.ActivityGoToResultBinding
import com.example.pt_b.retrofit.IRetrofit
import com.example.pt_b.retrofit.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class GoToResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityGoToResultBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoToResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val savedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("savedUri", android.net.Uri::class.java)
        } else {
            intent.getParcelableExtra<android.net.Uri>("savedUri")
        }
        if (savedUri != null) {
            binding.resultImage.setImageURI(savedUri)
            binding.btnSearch.setOnClickListener {
                intent = android.content.Intent(this, ResultActivity::class.java)
                intent.putExtra("savedUri", savedUri)
                startActivity(intent)

            }
        } else {
            Toast.makeText(this, "저장된 사진이 없습니다." , Toast.LENGTH_SHORT).show()
        }


    }

}