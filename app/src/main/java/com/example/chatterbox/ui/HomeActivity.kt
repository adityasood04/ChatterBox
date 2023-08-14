package com.example.chatterbox.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatterbox.MainActivity
import com.example.chatterbox.R
import com.example.chatterbox.adapters.UsersAdapter
import com.example.chatterbox.databinding.ActivityHomeBinding
import com.example.chatterbox.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var usersList:ArrayList<User>
    private  lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        usersList= ArrayList()
        usersAdapter= UsersAdapter(this,usersList)
        showPB()
        FirebaseFirestore.getInstance().collection("User").get()
            .addOnSuccessListener {querySnapshot->
                hidePB()
                usersList.clear()
                //get all users from db
                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        usersList.add(user)
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                hidePB()
                Toast.makeText(this@HomeActivity, "Some error encountered!", Toast.LENGTH_SHORT).show()
            }

        //setting up recyclerview
        binding.rcvChatUsers.layoutManager = LinearLayoutManager(this)
        binding.rcvChatUsers.adapter = usersAdapter


        binding.btnLogout.setOnClickListener {
            //logout user
            auth.signOut()
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
            finish()
        }

    }

    private fun showPB(){
        binding.pbUsersScreen.visibility = View.VISIBLE
    }
    private fun hidePB() {
        binding.pbUsersScreen.visibility = View.GONE
    }

}