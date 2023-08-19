package com.example.chatterbox.models

data class User(
    var id:String,
    val name:String,
    val email:String,
    val password:String? = null,
    val lastMessage:String? = null


){
    // Secondary constructor
        constructor() : this("", "","","","")
}
