package com.example.pt_b.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pt_b.databinding.ActivityGoToResultBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
            Toast.makeText(this, "사진이 저장되지 않았습니다." , Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToAPI(imageFile: File) {
        val MEDIA_TYPE_IMAGE ="image/*".toMediaTypeOrNull()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name, imageFile.asRequestBody(MEDIA_TYPE_IMAGE))
            .build()
    }
}