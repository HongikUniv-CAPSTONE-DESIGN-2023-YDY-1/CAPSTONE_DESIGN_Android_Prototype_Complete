package com.example.pt_b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

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
            binding.btnSearch.setOnClickListener {
                intent = android.content.Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "사진이 저장되지 않았습니다." , Toast.LENGTH_SHORT).show()
        }
    }
}