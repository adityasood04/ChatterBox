package com.example.chatterbox.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatterbox.R
import com.example.chatterbox.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)





    }
}