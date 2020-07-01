package com.muhyiddin.dsqis.model

class ChatList {
    var nama_pakar:String = ""
    var id_pakar:String = ""
    var nama_member:String = ""
    var id_member:String = ""
    var last_chat:String = ""
    var roomId:String = ""
    var time:String=""

    constructor()
    constructor(
        nama_pakar: String,
        id_pakar: String,
        nama_member: String,
        id_member: String,
        last_chat:String,
        roomId: String,
        time:String
    ) {
        this.nama_pakar = nama_pakar
        this.id_pakar = id_pakar
        this.nama_member = nama_member
        this.id_member = id_member
        this.roomId = roomId
        this.last_chat = last_chat
        this.time=time
    }
}