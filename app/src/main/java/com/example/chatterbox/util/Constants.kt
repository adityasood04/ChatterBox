package com.example.chatterbox.util

import com.google.firebase.auth.FirebaseAuth

class Constants {

    companion object {
        val  SENDER_ID :String = FirebaseAuth.getInstance().currentUser!!.uid
    }
}