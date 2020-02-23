package com.muhyiddin.dsqis.model

class Siswa {

    var id: String=""
    var nama: String=""
    var gender:String=""
    var ttl: String=""
    var nisn:String=""
    var kelas: String=""
    var alamat: String=""
    var nomor: String=""
    var cover:String? = null
//    val email: String
//    val password: String

    constructor()

    constructor(id:String,nama:String,gender:String,ttl:String,nisn:String,kelas:String,alamat:String,nomor:String,cover:String) {
        this.id=id
        this.nama = nama
        this.gender=gender
        this.ttl=ttl
        this.nisn=nisn
        this.kelas=kelas
        this.alamat=alamat
        this.nomor=nomor
        this.cover=cover

    }
}