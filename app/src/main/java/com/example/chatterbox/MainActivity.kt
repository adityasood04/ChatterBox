package com.example.chatterbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.chatterbox.databinding.ActivityMainBinding
import com.example.chatterbox.ui.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_bg)
        setContentView(binding.root)


        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }

    }
}