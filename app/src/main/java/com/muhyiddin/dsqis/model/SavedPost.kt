package com.muhyiddin.dsqis.model

class SavedPost {

    lateinit var savedPostId:String
    lateinit var postId:String

    constructor(){}

    constructor(savedPostId: String,postId:String){

        this.savedPostId=savedPostId
        this.postId=postId
    }

}