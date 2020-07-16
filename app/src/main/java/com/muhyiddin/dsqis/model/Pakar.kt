package com.muhyiddin.dsqis.model

import java.io.Serializable

class Pakar: Serializable {
    var id: String=""
    var namapakar: String=""
    var email:String=""
    var password:String=""
    var jenis:String=""
    var cover:String? = null
    var uid:String=""

    constructor()

    constructor(id:String,namapakar:String,email:String,password:String,jenis:String,cover:String,uid:String){

        this.id=id
        this.namapakar=namapakar
        this.email=email
        this.password=password
        this.jenis=jenis
        this.cover=cover
        this.uid=uid
    }
}