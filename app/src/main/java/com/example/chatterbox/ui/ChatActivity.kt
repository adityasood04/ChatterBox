package com.example.chatterbox.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatterbox.R
import com.example.chatterbox.adapters.ChatAdapter
import com.example.chatterbox.databinding.ActivityChatBinding
import com.example.chatterbox.models.Message
import com.example.chatterbox.models.Messageslist
import com.example.chatterbox.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar


class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    private lateinit var chatList:Messageslist
    private lateinit var chatsArrayList:ArrayList<Message>
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private val TAG = "adi chat"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiverID = intent.getStringExtra("RECEIVER_ID")!!
        val senderID = FirebaseAuth.getInstance().currentUser?.uid
        val username = intent.getStringExtra("USERNAME")!!

        //Receiver's name
        binding.tvChatUsername.text = username

        //sender and receiver ids
        senderRoom =  receiverID + senderID
        receiverRoom = senderID + receiverID

        //current time
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val timeFormat = SimpleDateFormat("h:mm a") // "2:40 PM" format
        val formattedTime = timeFormat.format(currentTime)

        chatsArrayList = ArrayList()
        chatList = Messageslist(chatsArrayList)

        //fetch previous chats from db
        getPreviousChats()

        binding.ivSend.setOnClickListener {
            if(binding.etMessage.text.toString().isNotBlank()){
                val message = Message(senderID!!,binding.etMessage.text.toString(),formattedTime)
                saveMessage(message)
            }
        }


    }

    private fun getPreviousChats() {
        FirebaseFirestore.getInstance().collection("Chats").document(senderRoom)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    if (it.result.exists()){
                        val messages = it.result.toObject(Messageslist::class.java)!!
                        chatList = messages
                        Log.i("adi msg", chatList.toString())
                    }
                    if(chatList.chats.isNotEmpty()){
                        setChatsInRCV(chatList)

                    }

                }
                else{
                    Toast.makeText(this@ChatActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    Log.i("adi error", "getPreviousChats: ${it.exception?.message} ")
                }
            }
    }

    private fun setChatsInRCV(chatList: Messageslist) {
        binding.rcvChats.layoutManager = LinearLayoutManager(this)
        binding.rcvChats.adapter = ChatAdapter(this,chatList.chats)
    }

    private fun saveMessage(message: Message) {
        chatList.chats.add(message)
        FirebaseFirestore.getInstance().collection("Chats").document(senderRoom)
            .set(chatList)
            .addOnSuccessListener {
                FirebaseFirestore.getInstance().collection("Chats").document(receiverRoom)
                    .set(chatList)
                binding.rcvChats.adapter?.notifyDataSetChanged()
                binding.etMessage.setText("")


            }
            .addOnFailureListener {
                Toast.makeText(this@ChatActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

    }
}