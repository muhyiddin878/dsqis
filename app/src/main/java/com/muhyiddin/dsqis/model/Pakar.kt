package com.muhyiddin.dsqis.model

class Pakar {
    var id: String=""
    var namapakar: String=""
    var email:String=""
    var password:String=""
    var jenis:String=""
    var cover:String? = null

    constructor()

    constructor(id:String,namapakar:String,email:String,password:String,jenis:String,cover:String){

        this.id=id
        this.namapakar=namapakar
        this.email=email
        this.password=password
        this.jenis=jenis
        this.cover=cover
    }
}