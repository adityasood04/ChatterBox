package com.example.chatterbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatterbox.R
import com.example.chatterbox.models.User

class UsersAdapter(
    private val context: Context,
    private val usersList: ArrayList<User>,
    private val listener: Listener
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val ivUserDP: ImageView = view.findViewById(R.id.ivUserDP)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val llUserItem: LinearLayout = view.findViewById(R.id.llUserItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.chat_user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    interface Listener{
        fun onUserClicked(userID:String)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.tvUsername.text = usersList[position].name
        holder.tvLastMessage.text = usersList[position].lastMessage
        holder.llUserItem.setOnClickListener {
            listener.onUserClicked(usersList[position].id)
        }

        //TODO load user dp using Picasso
    }
}