package com.muhyiddin.dsqis.model

class Comment {

    lateinit var commentid:String
    lateinit var commentWriterId:String
    lateinit var commentWriterName:String
    lateinit var commentWriterPic:String
    lateinit var commentDate:String
    lateinit var commentText:String
//    lateinit var PostId:String

    constructor()

    constructor(id:String, text:String, date:String, writerId:String, commentWriterName:String, commentWriterPic:String){
        this.commentid = id
        this.commentWriterId = writerId
        this.commentDate = date
        this.commentText = text
        this.commentWriterName = commentWriterName
        this.commentWriterPic = commentWriterPic
//        this.PostId=PostID
    }
}