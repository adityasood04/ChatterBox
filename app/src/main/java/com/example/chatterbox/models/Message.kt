package com.example.chatterbox.models

import java.sql.Time

data class Message(
    val senderID:String,
    val text:String,
    val time:String?=null
){
    constructor() :this
        ("","","")
}
