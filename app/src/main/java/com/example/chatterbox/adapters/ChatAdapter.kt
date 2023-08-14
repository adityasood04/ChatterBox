package com.example.chatterbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.R
import com.example.chatterbox.models.Message
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter (private val context: Context,
                   private val chatList:ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SenderChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvSentMessage = view.findViewById<TextView>(R.id.tvSentMessage)
    }
    class ReceivedChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvReceivedMessage = view.findViewById<TextView>(R.id.tvReceivedMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent_item,parent,false)
            SenderChatViewHolder(view)
        } else{
            val view = LayoutInflater.from(context).inflate(R.layout.message_received_item,parent,false)
            ReceivedChatViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return  chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage = chatList[position]
        //for sent message code = 1
        //for received message code = 0
        return if(FirebaseAuth.getInstance().currentUser!!.uid == currMessage.senderID){
            1
        } else{
            0
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage = chatList[position]

        //sent message
        if(holder.javaClass  == SenderChatViewHolder::class.java){
            val viewHolder = holder as SenderChatViewHolder
            viewHolder.tvSentMessage.text = currMessage.text
        }

        //received message
        else{
            val viewHolder = holder as ReceivedChatViewHolder
            viewHolder.tvReceivedMessage.text = currMessage.text
        }
    }
}