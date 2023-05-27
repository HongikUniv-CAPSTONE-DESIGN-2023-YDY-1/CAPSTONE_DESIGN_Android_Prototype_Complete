package com.example.pt_b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.example.pt_b.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val savedUri = intent.getParcelableExtra<android.net.Uri>("savedUri")
        if (savedUri != null) {
            binding.resultImage.setImageURI(savedUri)
        } else {
            Toast.makeText(this, "사진이 저장되지 않았습니다." , Toast.LENGTH_SHORT).show()
        }
    }
}