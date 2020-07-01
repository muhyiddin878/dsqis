package com.muhyiddin.dsqis.model

class Chat {


    var isRead:Boolean = false
    var message:String?=null
    var image:String?=null
    var pengirim:String = ""
    var type=""
    var id:String=""
    var time:String=""

    constructor()

    constructor(isRead: Boolean, message: String,image:String, pengirim: String,type:String,id:String,time:String) {
        this.isRead = isRead
        this.message = message
        this.image=image
        this.pengirim = pengirim
        this.type=type
        this.id=id
        this.time=time
    }
}