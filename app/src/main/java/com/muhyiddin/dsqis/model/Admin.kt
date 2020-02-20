package com.muhyiddin.dsqis.model

class Admin {
    var nama: String=""
    var email: String=""
    var password: String=""

    constructor()

    constructor(nama: String, email: String, password: String) {
        this.nama = nama
        this.email = email
        this.password = password
    }

//    fun getNama(): String {
//        return nama
//    }
//
//    fun getEmail(): String {
//        return email
//    }
//
//    fun getPassword(): String {
//        return password
//    }
}