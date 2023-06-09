package com.example.pt_b.ui

import android.media.MediaTimestamp
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pt_b.databinding.ActivityGoToResultBinding



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