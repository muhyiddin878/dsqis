package com.muhyiddin.dsqis.model

import java.io.Serializable

class Post:Serializable{

    lateinit var judul: String
    lateinit var isi: String
    lateinit var postDate: String
    var cover:String? = null
    var writerName:String? = null
    var writerId:String? = null
    var writerPic:String? = null
    lateinit var postId:String

    constructor(){}

    constructor(judul:String, isi:String, cover:String?, postDate:String, writerName:String?, writerId:String?, writerPic:String?, postId:String){
        this.judul = judul
        this.isi = isi
        this.cover = cover
        this.postDate = postDate
        this.writerName = writerName
        this.writerId = writerId
        this.writerPic = writerPic
        this.postId = postId
    }
}