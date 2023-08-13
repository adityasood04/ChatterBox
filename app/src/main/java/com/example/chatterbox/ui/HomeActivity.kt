package com.example.chatterbox.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.MainActivity
import com.example.chatterbox.R
import com.example.chatterbox.adapters.UsersAdapter
import com.example.chatterbox.databinding.ActivityHomeBinding
import com.example.chatterbox.models.User
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var usersList:ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        usersList = ArrayList()
        binding.rcvChatUsers.adapter = UsersAdapter(this,usersList)






    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuLogout){
            //logout user
            auth.signOut()
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
            finish()



            return true
        }
        return true
    }
}