package com.example.chatterbox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatterbox.R
import com.example.chatterbox.util.Constants
import com.example.chatterbox.util.Constants.Companion.SENDER_ID
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val receiverID = intent.getStringExtra("RECEIVER_ID")!!
        Log.i("adi sender", SENDER_ID)
        FirebaseFirestore.getInstance().collection("Chats").document(receiverID)
    }
}