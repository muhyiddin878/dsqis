package com.muhyiddin.dsqis.model

class Ortu {
    var id: String=""
    var namaortu: String=""
    var email:String=""
    var password:String=""

    constructor()

    constructor(id:String,namaortu:String,email:String,password:String){

        this.id=id
        this.namaortu=namaortu
        this.email=email
        this.password=password
    }
}