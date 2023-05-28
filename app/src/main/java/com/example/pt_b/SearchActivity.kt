package com.example.pt_b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pt_b.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val location: String = "cu"
        binding.searchCon.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("location",location)
            startActivity(intent)

        }
    }
}