package com.muhyiddin.dsqis.model

data class Nilai (

    var Penilaian_Sikap: HashMap<String, String>? = null,
    var Kelas_Pra_Akademik: HashMap<String, MutableList<Murajaah>>? = null,
    var Kelas_Komunal: HashMap<String, MutableList<Murajaah>>? = null,
    var Kelas_Sensori: HashMap<String, MutableList<Murajaah>>? = null,
    var Kelas_Komputer: HashMap<String, Any> ?=null,
    var Kelas_Murajaah: HashMap<String, MutableList<Murajaah>>? = null,
    var Ekstrakulikuler: HashMap<String, String>? = null,
    var Laporan_Perkembangan_Anak: HashMap<String, MutableList<Grafik>>? = null,
    var Saran_Guru: HashMap<String, String>? = null,
    var TbBb: HashMap<String, String>? = null,
    var Kondisi_Kesehatan: HashMap<String, String>? = null,
    var Evaluasi_Pertumbuhan_Anak: HashMap<String, String>? = null,
    var Absensi: HashMap<String, Int>? = null,
    var Evaluasi_Perkembangan_Anak: HashMap<String, String>? = null,
    var idSiswa:String?=null,
    var kelasSiswa:String?=null,
    var tanggal:String?=null

)





