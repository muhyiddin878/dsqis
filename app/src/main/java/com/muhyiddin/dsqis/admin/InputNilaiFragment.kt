package com.muhyiddin.dsqis.admin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhyiddin.dsqis.R
import kotlinx.android.synthetic.main.activity_buat_akun_fragment.*
import kotlinx.android.synthetic.main.activity_input_nilai_fragment.*
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.content.ContentValues.TAG
import com.google.firebase.firestore.*
import com.jjoe64.graphview.series.DataPoint
import com.muhyiddin.dsqis.model.*
import kotlinx.android.synthetic.main.activity_fragment_lihat_laporan.*
import java.text.SimpleDateFormat
import java.time.chrono.ThaiBuddhistEra
import java.util.*
import kotlin.collections.HashMap


class InputNilaiFragment : Fragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val data_siswa = mFirestore.collection("students")
    private lateinit var akun: MutableList<Siswa>
    private val siswa: MutableList<Siswa> = mutableListOf()
    private val namaSiswa: MutableList<String> = mutableListOf()
    private var siswaId: String? = null
    private var selectedIdSiswa:String?=null
    private val murajaah:MutableList<Murajaah> = mutableListOf()
    private val praAkademik:MutableList<Murajaah> = mutableListOf()
    private val komunal:MutableList<Murajaah> = mutableListOf()
    private val sensori:MutableList<Murajaah> = mutableListOf()
    private val perkembangan:MutableList<Grafik> = mutableListOf()
    private lateinit var kelas: String
    private lateinit var mapel: String
    private lateinit var minggu: String
    private lateinit var student: String
    private lateinit var isiMingguKomunal: String
    private lateinit var isiMingguSensori: String
    private lateinit var mingguke_sikap_sosial: String
    private lateinit var minggumurajaah: String
    private lateinit var mingguke_murajaah: String
    private var mingguke_perkembangan: Int=0
    private lateinit var idSiswa: String
    private var kelasSiswa=mutableListOf<String>()
    private lateinit var minggukee: String
    private lateinit var mingguke_sikap_sosial_spinner:Spinner
    private var Kelas_Murajaah = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Pra_Akademik = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Komunal = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Sensori = HashMap<String, MutableList<Murajaah>>()
    private var Laporan_Perkembangan_Anak = HashMap<String, MutableList<Grafik>>()
    private val listTanggal = mutableListOf<String>()
    private val mingguKomunal = mutableListOf<String>()
    private val mingguSensori = mutableListOf<String>()
    private val listNilai = mutableListOf<Nilai>()

    private val listMurajaah = mutableListOf<Murajaah>()
    private val listPraAkademik = mutableListOf<Murajaah>()
    private val listKomunal = mutableListOf<Murajaah>()
    private val listSensori = mutableListOf<Murajaah>()
    private val listPerkembangan = mutableListOf<Grafik>()
    private var nilai: Nilai? = null

    private lateinit var spinnerKelas:Spinner


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AdminActivity).setActionBarTitle("Input Nilai Siswa")
        return inflater.inflate(R.layout.activity_input_nilai_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.daftar_siswa)
        val spinnerKomunal= view.findViewById<Spinner>(R.id.daftar_komunal)
        val spinnerSensori=view.findViewById<Spinner>(R.id.daftar_sensori)
        spinnerKelas = view.findViewById<Spinner>(R.id.daftar_kelas)
        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran)
        mingguke_sikap_sosial_spinner = view.findViewById(R.id.mingguke)
        val mingguke_murajaah_spinner = view.findViewById<Spinner>(R.id.minggukeMurajaah)
        val mingguke_perkembangan_spinner = view.findViewById<Spinner>(R.id.minggukePerkembangan)
        val submit_nilai = view.findViewById<Button>(R.id.submit_nilai)
        val submit_nilai_perkembangan = view.findViewById<Button>(R.id.submit_nilai_perkembangan)
        val submit_nilai_murajaah = view.findViewById<Button>(R.id.submit_nilai_murajaah)
        val submit_nilai_akademik = view.findViewById<Button>(R.id.submit_nilai_akademik)


        data_siswa.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
                namaSiswa.clear()
                for (student in querySnapshot) {

                    siswa.add(student.toObject(Siswa::class.java))
                    val nama_siswa = student.toObject(Siswa::class.java)
                    namaSiswa.add(nama_siswa.nama)

                }

                val adapter =
                    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, namaSiswa)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.setAdapter(adapter)

            }
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                student = spinner.selectedItem.toString()
                val selectedSiswa = siswa[position]
                getIdSiswa(student)
                getKelasSiswa(selectedSiswa.id)
                selectedIdSiswa=selectedSiswa.id

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                kelas = spinnerKelas.selectedItem.toString()
                getNilaiEachStudent(selectedIdSiswa!!,kelas)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerKomunal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                isiMingguKomunal = spinnerKomunal.selectedItem.toString()
                nilai_komunal.setText("")
                ket_komunal.setText("")
                materi_komunal.setText("")
                setKomunalValue( isiMingguKomunal)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerSensori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                isiMingguSensori = spinnerSensori.selectedItem.toString()
                nilai_sensori.setText("")
                ket_sensori.setText("")
                materi_sensori.setText("")
                setSensoriValue(isiMingguSensori)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        mingguke_sikap_sosial_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                    minggu = mingguke_sikap_sosial_spinner.selectedItem.toString()
                    if (minggu == "1") {
                        mingguke_sikap_sosial = "1"
                        nilai_sikap_sosial.setText("")
                        ket_sikap_sosial.setText("")
                        materi_sikap_sosial.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "2") {
                        mingguke_sikap_sosial = "2"
                        nilai_sikap_sosial.setText("")
                        ket_sikap_sosial.setText("")
                        materi_sikap_sosial.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "3") {
                        mingguke_sikap_sosial = "3"
                        nilai_sikap_sosial.setText("")
                        ket_sikap_sosial.setText("")
                        materi_sikap_sosial.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "4") {
                        mingguke_sikap_sosial = "4"
                        nilai_sikap_sosial.setText("")
                        ket_sikap_sosial.setText("")
                        materi_sikap_sosial.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        mingguke_murajaah_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                    minggumurajaah = mingguke_murajaah_spinner.selectedItem.toString()
                    if (minggumurajaah == "1") {
                        mingguke_murajaah = "1"
                        nilai_murajaah.setText("")
                        ket_murajaah.setText("")
                        materi_murajaah.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "2") {
                        mingguke_murajaah = "2"
                        nilai_murajaah.setText("")
                        ket_murajaah.setText("")
                        materi_murajaah.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "3") {
                        mingguke_murajaah = "3"
                        nilai_murajaah.setText("")
                        ket_murajaah.setText("")
                        materi_murajaah.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "4") {
                        mingguke_murajaah = "4"
                        nilai_murajaah.setText("")
                        ket_murajaah.setText("")
                        materi_murajaah.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    }
//                    getIdSiswa(student)


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        mingguke_perkembangan_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                    minggu =  mingguke_perkembangan_spinner.selectedItem.toString()
                    if (minggu == "1") {
                        mingguke_perkembangan = 1
                        perkembangan_anak.setText("")
                        setPerkembanganValue(mingguke_perkembangan)
                    } else if (minggu == "2") {
                        mingguke_perkembangan = 2
                        perkembangan_anak.setText("")
                        setPerkembanganValue(mingguke_perkembangan)
                    } else if (minggu == "3") {
                        mingguke_perkembangan = 3
                        perkembangan_anak.setText("")
                        setPerkembanganValue(mingguke_perkembangan)
                    } else if (minggu == "4") {
                        mingguke_perkembangan = 4
                        perkembangan_anak.setText("")
                        setPerkembanganValue(mingguke_perkembangan)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        submit_nilai_perkembangan.setOnClickListener {
            submit_perkembangan()
        }
        submit_nilai_murajaah.setOnClickListener {
            Toast.makeText(requireContext(),"Data Ditambahkan ke Lokal",Toast.LENGTH_SHORT).show()
            submit_murajaah1()
        }
        submit_nilai_akademik.setOnClickListener{
            Toast.makeText(requireContext(),"Data Ditambahkan ke Lokal",Toast.LENGTH_SHORT).show()
            submit_akademik()
        }
        submit_nilai_sensori.setOnClickListener{

             submit_sensori()
        }
        submit_nilai_komunal.setOnClickListener{

            submit_komunal()
        }

        submit_nilai.setOnClickListener {


            val nilai = Nilai()

            val Penilaian_Sikap = HashMap<String, String>()
            Penilaian_Sikap.put("Sikap Spiritual", sikap_spiritual.text.toString())
            Penilaian_Sikap.put("Sikap Sosial", sikap_sosial.text.toString())

            val Kelas_Komputer = HashMap<String, Any>()
            Kelas_Komputer.put("Materi", nilaikomputer.text.toString())
            Kelas_Komputer.put("Keterangan", ket_komputer.text.toString())
            Kelas_Komputer.put("Nilai", nilai_komputer.text.toString())



            val Ekstrakulikuler = HashMap<String, String>()
            Ekstrakulikuler.put("Nama Ekstra", nama_ekstra.text.toString())
            Ekstrakulikuler.put("Keterangan", ket_ekstra.text.toString())

            val Saran_Guru = HashMap<String, String>()
            Saran_Guru.put("Saran Guru", saran_guru.text.toString())

            val TbBb = HashMap<String, String>()
            TbBb.put("Tinggi Badan", tinggi_badan.text.toString())
            TbBb.put("Berat Badan", berat_badan.text.toString())

            val Kondisi_Kesehatan = HashMap<String, String>()
            Kondisi_Kesehatan.put("Kesehatan Penglihatan", penglihatan.text.toString())
            Kondisi_Kesehatan.put("Kesehatan Pendengaran", pendengaran.text.toString())
            Kondisi_Kesehatan.put("Kondisi Gigi", gigi.text.toString())

            val Evaluasi_Pertumbuhan_Anak = HashMap<String, String>()
            Evaluasi_Pertumbuhan_Anak.put("Kondisi Saat Ini", kondisi_saat_ini.text.toString())
            Evaluasi_Pertumbuhan_Anak.put("Kondisi Ideal", kondisi_ideal.text.toString())
            Evaluasi_Pertumbuhan_Anak.put("Saran Dokter", saran_dokter.text.toString())

            val Absensi = HashMap<String, Int>()
            val izin=izin.text.toString()
            val sakit= sakit.text.toString()
            val tanpa= tidak_ada_keterangan.text.toString()
            izin?.toIntOrNull()?.let { it1 -> Absensi.put("Izin", it1) }
            sakit?.toIntOrNull()?.let{itl->Absensi.put("Sakit", itl) }
            tanpa?.toIntOrNull()?.let{itl->Absensi.put("Tanpa Keterangan", itl) }




            val Evaluasi_Perkembangan_Anak = HashMap<String, String>()
            Evaluasi_Perkembangan_Anak.put(
                    "Kondisi Psikologi Saat Ini",
                    kondisi_psikologi_saat_ini.text.toString()
                )
            Evaluasi_Perkembangan_Anak.put(
                    "Kondisi Psikologi Ideal",
                    kondisi_ideal_psikologi.text.toString()
                )
            Evaluasi_Perkembangan_Anak.put("Saran Psikolog", saran_psikolog.text.toString())
            Evaluasi_Perkembangan_Anak.put(
                    "Kondisi Okupasi Saat Ini",
                    kondisi_okupasi_saat_ini.text.toString()
                )
            Evaluasi_Perkembangan_Anak.put(
                    "Kondisi Okupasi Ideal",
                    kondisi_ideal_okupasi.text.toString()
                )
            Evaluasi_Perkembangan_Anak.put("Saran Okupasi", saran_okupasi.text.toString())

            val Kelas_Murajaah2 = HashMap<String, MutableList<Murajaah>>()
            val praAkademik2=HashMap<String, MutableList<Murajaah>>()
            val perkembangan2=HashMap<String, MutableList<Grafik>>()
            val komunal2=HashMap<String, MutableList<Murajaah>>()
            val sensori2=HashMap<String, MutableList<Murajaah>>()



            if (tanggal_nilai.selectedItemPosition == 0) {
                nilai.Penilaian_Sikap = Penilaian_Sikap
                nilai.Kelas_Komputer = Kelas_Komputer
                nilai.Ekstrakulikuler = Ekstrakulikuler
                nilai.Saran_Guru = Saran_Guru
                nilai.TbBb = TbBb
                nilai.Kondisi_Kesehatan = Kondisi_Kesehatan
                nilai.Evaluasi_Pertumbuhan_Anak = Evaluasi_Pertumbuhan_Anak
                nilai.Absensi = Absensi
                nilai.Evaluasi_Perkembangan_Anak = Evaluasi_Perkembangan_Anak
                nilai.kelasSiswa=kelas
                nilai.idSiswa=idSiswa


                if(Kelas_Murajaah.size>0){
                    nilai.Kelas_Murajaah=Kelas_Murajaah
                }else{

                    Kelas_Murajaah2.put("Kelas Murajaah",murajaah)
                    nilai.Kelas_Murajaah = Kelas_Murajaah2
                }
                if(Kelas_Pra_Akademik.size>0){
                    nilai.Kelas_Pra_Akademik=Kelas_Pra_Akademik
                }else{

                    praAkademik2.put("Kelas Pra Akademik",praAkademik)
                    nilai.Kelas_Pra_Akademik= praAkademik2
                }


                if(Kelas_Komunal.size>0){
                    nilai.Kelas_Komunal=Kelas_Komunal
                }else{

                    komunal2.put("Kelas Komunikasi Lanjutan",komunal)
                    nilai.Kelas_Komunal = komunal2
                }


                if(Kelas_Sensori.size>0){
                    nilai.Kelas_Sensori=Kelas_Sensori
                }else{

                    sensori2.put("Kelas Sensori Integrasi",sensori)
                    nilai.Kelas_Sensori = sensori2
                }

                if(Laporan_Perkembangan_Anak.size>0){
                    nilai.Laporan_Perkembangan_Anak=Laporan_Perkembangan_Anak
                }else{

                    perkembangan2.put("Laporan Perkembangan Anak",perkembangan)
                    nilai.Laporan_Perkembangan_Anak=perkembangan2
                }
//                nilai.Laporan_Perkembangan_Anak=Laporan_Perkembangan_Anak
                nilai.tanggal=SimpleDateFormat("dd-MM-yyyy").format(Date())
            } else {

                this.nilai?.Penilaian_Sikap?.forEach {
                    if (Penilaian_Sikap?.get(it.key) == null || Penilaian_Sikap?.get(it.key) == "") {
                        Penilaian_Sikap?.put(it.key, it.value)
                    }
                }
                nilai.Penilaian_Sikap= Penilaian_Sikap

                this.nilai?.Kelas_Komputer?.forEach {
                    if (Kelas_Komputer?.get(it.key) == null || Kelas_Komputer?.get(it.key) == "") {
                        Kelas_Komputer?.put(it.key, it.value)
                    }
                }
                nilai.Kelas_Komputer = Kelas_Komputer

                this.nilai?.Ekstrakulikuler?.forEach {
                    if (Ekstrakulikuler?.get(it.key) == null || Ekstrakulikuler?.get(it.key) == "") {
                        Ekstrakulikuler?.put(it.key, it.value)
                    }
                }
                nilai.Ekstrakulikuler = Ekstrakulikuler

                this.nilai?.Saran_Guru?.forEach {
                    if (Saran_Guru?.get(it.key) == null || Saran_Guru?.get(it.key) == "") {
                        Saran_Guru?.put(it.key, it.value)
                    }
                }
                nilai.Saran_Guru = Saran_Guru

                this.nilai?.TbBb?.forEach {
                    if (TbBb?.get(it.key) == null || TbBb?.get(it.key) == "") {
                        TbBb?.put(it.key, it.value)
                    }
                }
                nilai.TbBb= TbBb

                this.nilai?.Kondisi_Kesehatan?.forEach {
                    if (Kondisi_Kesehatan?.get(it.key) == null || Kondisi_Kesehatan?.get(it.key) == "") {
                        Kondisi_Kesehatan?.put(it.key, it.value)
                    }
                }
                nilai.Kondisi_Kesehatan= Kondisi_Kesehatan

                this.nilai?.Absensi?.forEach {
                    if (Absensi?.get(it.key) == null ) {
                        Absensi?.put(it.key, it.value)
                    }
                }
                nilai.Absensi= Absensi

                this.nilai?.Evaluasi_Perkembangan_Anak?.forEach {
                    if (Evaluasi_Perkembangan_Anak?.get(it.key) == null || Evaluasi_Perkembangan_Anak?.get(it.key) == "") {
                        Evaluasi_Perkembangan_Anak?.put(it.key, it.value)
                    }
                }
                nilai.Evaluasi_Perkembangan_Anak= Evaluasi_Perkembangan_Anak

                this.nilai?.Absensi?.forEach {
                    if (Absensi?.get(it.key) == null || Kondisi_Kesehatan?.get(it.key) == "") {
                        Absensi?.put(it.key, it.value)
                    }
                }
                nilai.Absensi= Absensi


                if(Kelas_Murajaah.size>0){
                    nilai.Kelas_Murajaah = Kelas_Murajaah
                }else{
                    val isiMur=this.nilai?.Kelas_Murajaah?.get("Kelas Murajaah")
                    Kelas_Murajaah2.put("Kelas Murajaah",isiMur!!)
                    nilai.Kelas_Murajaah = Kelas_Murajaah2
                }
                if(Kelas_Pra_Akademik.size>0){
                    nilai.Kelas_Pra_Akademik=Kelas_Pra_Akademik
                }else{
                    val isiPra=this.nilai?.Kelas_Pra_Akademik?.get("Kelas Pra Akademik")
                    praAkademik2.put("Kelas Pra Akademik",isiPra!!)
                    nilai.Kelas_Pra_Akademik= praAkademik2
                }
                if(Laporan_Perkembangan_Anak.size>0){
                    nilai.Laporan_Perkembangan_Anak=Laporan_Perkembangan_Anak
                }else{
                    val isiPer=this.nilai?.Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")
                    perkembangan2.put("Laporan Perkembangan Anak",isiPer!!)
                    nilai.Laporan_Perkembangan_Anak=perkembangan2
                }

                if(Kelas_Komunal.size>0){
                    nilai.Kelas_Komunal=Kelas_Komunal
                }else{
                    val isiKom=this.nilai?.Kelas_Komunal?.get("Kelas Komunikasi Lanjutan")
                    komunal2.put("Kelas Komunikasi Lanjutan",isiKom!!)
                    nilai.Kelas_Komunal=komunal2
                }

                if(Kelas_Sensori.size>0){
                    nilai.Kelas_Sensori=Kelas_Sensori
                }else{
                    val isiSen=this.nilai?.Kelas_Sensori?.get("Kelas Sensori Integrasi")
                    sensori2.put("Kelas Sensori Integrasi",isiSen!!)
                    nilai.Kelas_Sensori=sensori2
                }

                nilai.tanggal= this.nilai?.tanggal
                nilai.kelasSiswa= this.nilai?.kelasSiswa
                nilai.idSiswa= this.nilai?.idSiswa

            }

            inputNilai(nilai)


        }

        mata_pelajaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                mapel = mata_pelajaran.selectedItem.toString()
                if (mapel == "Penilaian Sikap") {
                   mapel1()

                }
                if (mapel == "Kelas Pra Akademik") {
                    mapel1()

                }
                if (mapel == "Kelas Komputer") {
                    mapel1()

                }
                if (mapel == "Kelas Muraja'ah") {
                    mapel1()

                }
                if (mapel == "Ekstrakulikuler") {
                    mapel1()

                }
                if (mapel == "Laporan Perkembangan Anak") {
                    mapel1()

                }
                if (mapel == "Saran Guru") {
                    mapel1()

                }
                if (mapel == "Tinggi dan Berat Badan") {
                    mapel1()
                }
                if (mapel == "Kondisi Kesehatan") {
                    mapel1()

                }
                if (mapel == "Evaluasi Pertumbuhan Anak") {
                    mapel1()

                }
                if (mapel == "Absensi") {
                    mapel1()
                }
                if (mapel == "Evaluasi Perkembangan Anak") {
                    mapel1()

                }
                if(mapel=="Kelas Komunikasi Lanjutan"){
                    mapel1()
                }
                if(mapel=="Kelas Sensori Integrasi"){
                    mapel1()
                }
                if (mapel=="-"){
                    mapel1()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        tanggal_nilai.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {



                if (tanggal_nilai.selectedItemPosition==0) {
                    mingguKomunal.clear()
                    mingguSensori.clear()
                    mingguKomunal.add("Tambah Minggu Baru")
                    mingguSensori.add("Tambah Minggu Baru")
                    val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, mingguKomunal)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerKomunal.setAdapter(adapter)

                    val adapter2 = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, mingguSensori)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSensori.setAdapter(adapter2)

                    sikap_spiritual.setText("")
                    sikap_sosial.setText("")

                    nilaikomputer.setText("")
                    ket_komputer.setText("")
                    nilai_komputer.setText("")

                    nama_ekstra.setText("")
                    ket_ekstra.setText("")

                    saran_guru.setText("")

                    tinggi_badan.setText("")
                    berat_badan.setText("")

                    penglihatan.setText("")
                    pendengaran.setText("")
                    daya_tahan.setText("")
                    gigi.setText("")

                    kondisi_saat_ini.setText("")
                    kondisi_ideal.setText("")
                    saran_dokter.setText("")

                    izin.setText("")
                    sakit.setText("")
                    tidak_ada_keterangan.setText("")

                    kondisi_psikologi_saat_ini.setText("")
                    kondisi_ideal_psikologi.setText("")
                    saran_psikolog.setText("")
                    kondisi_okupasi_saat_ini.setText("")
                    tv_kondisi_ideal_okupasi.setText("")
                    saran_okupasi.setText("")

                } else {
                    nilai = listNilai.find {
                        it.tanggal == tanggal_nilai.selectedItem
                    }
                    val coba=nilai?.Kelas_Sensori?.get("Kelas Sensori Integrasi")
                    Log.d("COBA","${coba}")
                    siswaId = nilai?.idSiswa

                    when(mata_pelajaran.selectedItem) {
                        "Penilaian Sikap" -> {
                            if (nilai?.Penilaian_Sikap?.get("Sikap Spiritual")!=null){
                                sikap_spiritual.setText("${nilai?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                            }
                            if(nilai?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                                sikap_sosial.setText("${nilai?.Penilaian_Sikap?.get("Sikap Sosial")}")
                            }
                        }
                        "Kelas Komunikasi Lanjutan"->{

                            if(nilai?.Kelas_Komunal!=null){
                                Kelas_Komunal = nilai?.Kelas_Komunal!!
                                Kelas_Komunal?.get("Kelas Komunikasi Lanjutan")?.forEach {
                                    listKomunal.add(it)
                                    mingguKomunal.add(it.minggu!!)
                                }
                                Log.d("listKomunal","${listKomunal}")
                                val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, mingguKomunal)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinnerKomunal.setAdapter(adapter)
                            }
                        }
                        "Kelas Sensori Integrasi"->{

                            if(nilai?.Kelas_Sensori!=null){
                                Kelas_Sensori = nilai?.Kelas_Sensori!!
                                Kelas_Sensori?.get("Kelas Sensori Integrasi")?.forEach {
                                    listSensori.add(it)
                                    mingguSensori.add(it.minggu!!)
                                }
                                val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, mingguSensori)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinnerSensori.setAdapter(adapter)
                            }
                        }
                        "Kelas Pra Akademik" -> {
                            if(nilai?.Kelas_Pra_Akademik!=null){
                                Kelas_Pra_Akademik = nilai?.Kelas_Pra_Akademik!!
                                Kelas_Pra_Akademik?.get("Kelas Pra Akademik")?.forEach {
                                    listPraAkademik.add(it)
                                }
                            }

                        }
                        "Kelas Komputer" -> {
                            if(nilai?.Kelas_Komputer?.get("Materi")!=null){
                                nilaikomputer.setText("${nilai?.Kelas_Komputer?.get("Materi")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Keterangan")!=null){
                                ket_komputer.setText("${nilai?.Kelas_Komputer?.get("Keterangan")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Nilai")!=null){
                                nilai_komputer.setText("${nilai?.Kelas_Komputer?.get("Nilai")}")
                            }

                        }
                        "Kelas Muraja'ah" -> {
                            if(nilai?.Kelas_Murajaah!=null){
                                Kelas_Murajaah = nilai?.Kelas_Murajaah!!
                                Kelas_Murajaah?.get("Kelas Murajaah")?.forEach {
                                    listMurajaah.add(it)
                                }
                            }

                        }
                        "Ekstrakulikuler" -> {
                            if(nilai?.Ekstrakulikuler?.get("Nama Ekstra")!=null){
                                nama_ekstra.setText("${nilai?.Ekstrakulikuler?.get("Nama Ekstra")}")
                            }
                            if(nilai?.Ekstrakulikuler?.get("Keterangan")!=null){
                                ket_ekstra.setText("${nilai?.Ekstrakulikuler?.get("Keterangan")}")
                            }

                        }
                        "Laporan Perkembangan Anak" -> {
                            if(nilai?.Laporan_Perkembangan_Anak!=null){
                                Laporan_Perkembangan_Anak = nilai?.Laporan_Perkembangan_Anak!!
                                Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")?.forEach {
                                    listPerkembangan.add(it)
                                }
                            }

                        }
                        "Saran Guru" -> {
                            if(nilai?.Saran_Guru?.get("Saran Guru")!=null){
                                saran_guru.setText("${nilai?.Saran_Guru?.get("Saran Guru")}")
                            }

                        }
                        "Tinggi dan Berat Badan" -> {
                            if(nilai?.TbBb?.get("Tinggi Badan")!=null){
                                tinggi_badan.setText("${nilai?.TbBb?.get("Tinggi Badan")}")
                            }
                            if(nilai?.TbBb?.get("Berat Badan")!=null){
                                berat_badan.setText("${nilai?.TbBb?.get("Berat Badan")}")
                            }

                        }
                        "Kondisi Kesehatan" -> {
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                                penglihatan.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                                pendengaran.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                                daya_tahan.setText("${nilai?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                                gigi.setText("${nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                            }

                        }
                        "Evaluasi Pertumbuhan Anak" -> {
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                                kondisi_saat_ini.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                                kondisi_ideal.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                                saran_dokter.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                            }

                        }
                        "Absensi" -> {

                            if(nilai?.Absensi?.get("Izin")!=null){
                                izin.setText("${nilai?.Absensi?.get("Izin")}")
                            }
                            if(nilai?.Absensi?.get("Sakit")!=null){
                                sakit.setText("${nilai?.Absensi?.get("Sakit")}")
                            }
                            if(nilai?.Absensi?.get("Tanpa Keterangan")!=null){
                                tidak_ada_keterangan.setText("${nilai?.Absensi?.get("Tanpa Keterangan")}")
                            }

                        }
                        "Evaluasi Perkembangan Anak" -> {
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                                kondisi_psikologi_saat_ini.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                                kondisi_ideal_psikologi.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                                saran_psikolog.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                                kondisi_okupasi_saat_ini.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                                kondisi_ideal_okupasi.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                                saran_okupasi.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                            }
                        }

                    }
                }


            }

        }

    }

    private fun setMurajaahValue(minggukeMurajaah: String) {
        val murajaah = listMurajaah.find {it.minggu == minggukeMurajaah}
        if (murajaah!=null) {
            val nilaii=murajaah.nilai
            nilai_murajaah.setText(Integer.toString(nilaii!!))
            ket_murajaah.setText(murajaah.keterangan)
            materi_murajaah.setText(murajaah.materi)
        }
    }

    private fun setPraAkademikValue(minggukePraAkademik:String){
        val praAkademik = listPraAkademik.find { it.minggu==minggukePraAkademik }
        if(praAkademik!=null){
            val nilai=praAkademik.nilai
            materi_sikap_sosial.setText(praAkademik.materi)
            ket_sikap_sosial.setText(praAkademik.keterangan)
            nilai_sikap_sosial.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setKomunalValue(minggukeKomunal:String){
        val komunal= listKomunal.find { it.minggu==minggukeKomunal}
        if(komunal!=null){
            val nilai=komunal.nilai
            materi_komunal.setText(komunal.materi)
            ket_komunal.setText(komunal.keterangan)
            nilai_komunal.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setSensoriValue(minggukeSensori:String){
        val sensori= listSensori.find { it.minggu==minggukeSensori}
        if(sensori!=null){
            val nilai=sensori.nilai
            materi_sensori.setText(sensori.materi)
            ket_sensori.setText(sensori.keterangan)
            nilai_sensori.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setPerkembanganValue(mingguPerkembangan:Int){
        val perkembangan=listPerkembangan.find { it.minggu==mingguke_perkembangan }
        if(perkembangan!=null){
            val nilai=perkembangan.angka
            perkembangan_anak.setText(Integer.toString(nilai!!))
        }
    }


    private fun getNilaiEachStudent(idSiswa: String,kelas1:String) {
        listTanggal.clear()
        listTanggal.add("Tambah Data Baru")
        var ref = mFirestore.collection("nilai")
        ref.whereEqualTo("idSiswa", idSiswa).whereEqualTo("kelasSiswa",kelas1).get()
            .addOnSuccessListener {
                listNilai.clear()
                it.forEach {
                    val nilai = it.toObject(Nilai::class.java)
                        listNilai.add(nilai)
                }
                Log.d("Isi ListNilai","${listNilai}")

                if (listNilai.size > 0) {
                    listNilai.map {nilai ->
                        nilai.tanggal
                    }.forEach {
                        listTanggal.add(it.toString())
                    }
                }

                tanggal_nilai.setAdapter(null)
                tanggal_nilai.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, listTanggal)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


    private fun inputNilai(nilai: Nilai) {

        if (tanggal_nilai.selectedItemPosition == 0) {
            val sekarang=SimpleDateFormat("dd-MM-yyyy").format(Date())
            val tanggal=listTanggal.find {
                it==sekarang
            }
                if(tanggal==null){
                    mFirestore.collection("nilai").add(nilai)?.addOnSuccessListener {
                        Toast.makeText(context, "Nilai Baru Berhasil Ditambahkan", Toast.LENGTH_SHORT)
                            .show()

                    }?.addOnFailureListener {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()

                    }
                }else{
                    mFirestore.collection("nilai")
                        .whereEqualTo("idSiswa", siswaId).whereEqualTo("kelasSiswa",kelas)
                        .get()
                        .addOnSuccessListener {
                            it.forEach{itl ->
                                itl.reference.update(
                                    "idSiswa", nilai.idSiswa,
                                    "kelasSiswa", nilai.kelasSiswa,
                                    "penilaian_Sikap",nilai.Penilaian_Sikap,
                                    "kelas_Komunal",nilai.Kelas_Komunal,
                                    "kelas_Sensori",nilai.Kelas_Sensori,
                                    "kelas_Pra_Akademik", nilai.Kelas_Pra_Akademik,
                                    "kelas_Komputer",nilai.Kelas_Komputer,
                                    "kelas_Murajaah",nilai.Kelas_Murajaah,
                                    "ekstrakulikuler",nilai.Ekstrakulikuler,
                                    "laporan_Perkembangan_Anak",nilai.Laporan_Perkembangan_Anak,
                                    "saran_Guru",nilai.Saran_Guru,
                                    "tinggi_dan_Berat_Badan",nilai.TbBb,
                                    "kondisi_Kesehatan",nilai.Kondisi_Kesehatan,
                                    "evaluasi_Pertumbuhan_Anak",nilai.Evaluasi_Pertumbuhan_Anak,
                                    "absensi",nilai.Absensi,
                                    "evaluasi_Perkembangan_Anak",nilai.Evaluasi_Perkembangan_Anak
                                )
                            }

                            Toast.makeText(requireContext(),"Nilai Berhasil Di Update",Toast.LENGTH_SHORT).show()
                        }
                }

        } else {
            mFirestore.collection("nilai")
                .whereEqualTo("idSiswa", siswaId).whereEqualTo("kelasSiswa",kelas)
                .get()
                .addOnSuccessListener {
                    it.forEach{itl ->
                        itl.reference.update(
                            "idSiswa", nilai.idSiswa,
                            "kelasSiswa", nilai.kelasSiswa,
                            "penilaian_Sikap",nilai.Penilaian_Sikap,
                            "kelas_Komunal",nilai.Kelas_Komunal,
                            "kelas_Sensori",nilai.Kelas_Sensori,
                            "kelas_Pra_Akademik", nilai.Kelas_Pra_Akademik,
                            "kelas_Komputer",nilai.Kelas_Komputer,
                            "kelas_Murajaah",nilai.Kelas_Murajaah,
                            "ekstrakulikuler",nilai.Ekstrakulikuler,
                            "laporan_Perkembangan_Anak",nilai.Laporan_Perkembangan_Anak,
                            "saran_Guru",nilai.Saran_Guru,
                            "tinggi_dan_Berat_Badan",nilai.TbBb,
                            "kondisi_Kesehatan",nilai.Kondisi_Kesehatan,
                            "evaluasi_Pertumbuhan_Anak",nilai.Evaluasi_Pertumbuhan_Anak,
                            "absensi",nilai.Absensi,
                            "evaluasi_Perkembangan_Anak",nilai.Evaluasi_Perkembangan_Anak
                        )
                    }

                    Toast.makeText(requireContext(),"Nilai Berhasil Di Update",Toast.LENGTH_SHORT).show()

                }
        }

        perkembangan.clear()
        murajaah.clear()
        praAkademik.clear()

    }
    private fun submit_perkembangan(){
        val perkembangan2=Grafik(perkembangan_anak.text.toString().toInt(), mingguke_perkembangan)
        perkembangan.add(perkembangan2)
        perkembangan.forEach { per ->
            val listPerkembangan= Laporan_Perkembangan_Anak.get("Laporan Perkembangan Anak")
            val index = listPerkembangan?.indexOfFirst {
                it.minggu == per.minggu
            }
            if (index != -1) {
                index?.let { listPerkembangan[it] = per }
            } else {
                listPerkembangan.add(per)
            }

        }

    }

    private fun submit_murajaah1(){
        val murajaah2 = Murajaah(mingguke_murajaah,materi_murajaah.text.toString(),ket_murajaah.text.toString(),nilai_murajaah.text.toString().toInt())
        murajaah.add(murajaah2)
        murajaah.forEach { mur ->
            val listMurajaah = Kelas_Murajaah.get("Kelas Murajaah")
            val index = listMurajaah?.indexOfFirst {
                it.minggu == mur.minggu
            }
            if (index != -1) {
                index?.let { listMurajaah[it] = mur }
            } else {
                listMurajaah.add(mur)
            }

        }

    }

    private fun submit_akademik(){

        val akademik= Murajaah(mingguke_sikap_sosial,materi_sikap_sosial.text.toString(),ket_sikap_sosial.text.toString(),nilai_sikap_sosial.text.toString().toInt())
        praAkademik.add(akademik)
        praAkademik.forEach { akad->
            val listPraAkademik= Kelas_Pra_Akademik.get("Kelas Pra Akademik")
            val index = listPraAkademik?.indexOfFirst {
                it.minggu == akad.minggu
            }
            if (index != -1) {
                index?.let { listPraAkademik[it] = akad }
            } else {
                listPraAkademik.add(akad)
            }
        }


    }


    private fun submit_komunal(){
        var komunal1:Murajaah
        if(isiMingguKomunal!=null){
            if(isiMingguKomunal=="Tambah Minggu Baru"){
                Log.d("MASUK ATAS","MASUK")
                if(mingguke_komunal.text.toString()==""){
                    Toast.makeText(requireContext(),"Minggu Harus Di Isi!!",Toast.LENGTH_SHORT).show()
                }else{
                    komunal1= Murajaah(mingguke_komunal.text.toString(),materi_komunal.text.toString(),ket_komunal.text.toString(),nilai_komunal.text.toString().toInt())
                    komunal.add(komunal1)
                }
            }else{
                Log.d("MASUK BAWAH","MASUK")
                komunal1= Murajaah(isiMingguKomunal,materi_komunal.text.toString(),ket_komunal.text.toString(),nilai_komunal.text.toString().toInt())
                komunal.add(komunal1)
            }

            komunal.forEach { kom->
                val komunal2= Kelas_Komunal.get("Kelas Komunikasi Lanjutan")
                val index = komunal2?.indexOfFirst {
                    it.minggu == kom.minggu
                }
                if (index != -1) {
                    index?.let { komunal2[it] = kom}
                } else {
                    komunal2.add(kom)
                }
                Toast.makeText(requireContext(),"Data Ditambahkan ke Lokal",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun submit_sensori(){
        var sensori1:Murajaah
        val cek=mingguke_sensori.text.toString()
        if(isiMingguSensori!=null){
            if(isiMingguSensori=="Tambah Minggu Baru"){
                if(cek==""){
                    Toast.makeText(requireContext(),"Minggu Harus Di Isi!!",Toast.LENGTH_SHORT).show()
                }else{
                    sensori1= Murajaah(mingguke_sensori.text.toString(),materi_sensori.text.toString(),ket_sensori.text.toString(),nilai_sensori.text.toString().toInt())
                    sensori.add(sensori1)
                }
            }else{
                sensori1= Murajaah(isiMingguSensori,materi_sensori.text.toString(),ket_sensori.text.toString(),nilai_sensori.text.toString().toInt())
                sensori.add(sensori1)
            }

            sensori.forEach { sen->
                val sensori2= Kelas_Sensori.get("Kelas Sensori Integrasi")
                val index = sensori2?.indexOfFirst {
                    it.minggu == sen.minggu
                }
                if (index != -1) {
                    index?.let { sensori2[it] = sen}
                } else {
                    sensori2.add(sen)
                }

                Toast.makeText(requireContext(),"Data Ditambahkan ke Lokal",Toast.LENGTH_SHORT).show()
            }
        }



    }



    private fun getIdSiswa(namaSiswa: String) {
        mFirestore.collection("students")
            .whereEqualTo("nama", namaSiswa)
            .get()
            .addOnSuccessListener {
                for (siswa in it) {
                    val sis = siswa.toObject(Siswa::class.java)
                    idSiswa = sis.id
                }

            }
    }

    private fun getKelasSiswa(idSiswa: String){
        lateinit var kelastunggal:String
        mFirestore.collection("students")
            .whereEqualTo("id", idSiswa)
            .get()
            .addOnSuccessListener {
                for (siswa in it) {
                    kelasSiswa.clear()
                    val sis = siswa.toObject(Siswa::class.java)
                    kelastunggal=sis.kelas
                    kelasSiswa.add(sis.kelas)
                }

                Log.d("Kelas dari students","${kelasSiswa}")
                mFirestore.collection("nilai").whereEqualTo("idSiswa", idSiswa)
                    .get()
                    .addOnSuccessListener {
                        for (siswa in it){
                            val sis = siswa.toObject(Nilai::class.java)
                            if(sis.kelasSiswa!=kelastunggal){
                                kelasSiswa.add(sis.kelasSiswa!!)
                            }
                        }
                    }

                val adapter =ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, kelasSiswa)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerKelas.setAdapter(adapter)


            }
    }

    private fun setMinggu(id:String){
        if (id!=null){
        val arrayMinggu = resources.getStringArray(R.array.minggu)
        val pos = arrayMinggu.indexOf(minggukee)
        mingguke_sikap_sosial_spinner.setSelection(pos)
        }
    }

    fun mapel1(){
        tv_spiritual.visibility = View.GONE
        sikap_spiritual.visibility = View.GONE
        tv_sosial.visibility = View.GONE
        sikap_sosial.visibility = View.GONE

        tv_mingguke.visibility = View.GONE
        mingguke.visibility = View.GONE
        tv_materi.visibility = View.GONE
        materi_sikap_sosial.visibility = View.GONE
        tv_ket.visibility = View.GONE
        ket_sikap_sosial.visibility = View.GONE
        tv_nilai_sosial.visibility = View.GONE
        nilai_sikap_sosial.visibility = View.GONE
        submit_nilai_akademik.visibility=View.GONE

        daftar_komunal.visibility=View.GONE
        tv_mingguke_komunal.visibility = View.GONE
        mingguke_komunal.visibility = View.GONE
        tv_materi_komunal.visibility = View.GONE
        materi_komunal.visibility = View.GONE
        tv_ket_komunal.visibility = View.GONE
        ket_komunal.visibility = View.GONE
        tv_nilai_komunal.visibility = View.GONE
        nilai_komunal.visibility = View.GONE
        submit_nilai_komunal.visibility=View.GONE



        daftar_sensori.visibility=View.GONE
        tv_mingguke_sensori.visibility = View.GONE
        mingguke_sensori.visibility = View.GONE
        tv_materi_sensori.visibility = View.GONE
        materi_sensori.visibility = View.GONE
        tv_ket_sensori.visibility = View.GONE
        ket_sensori.visibility = View.GONE
        tv_nilai_sensori.visibility = View.GONE
        nilai_sensori.visibility = View.GONE
        submit_nilai_sensori.visibility=View.GONE

        tv_materi_komputer.visibility = View.GONE
        nilaikomputer.visibility = View.GONE
        tv_ket_komputer.visibility = View.GONE
        ket_komputer.visibility = View.GONE
        tv_nilai_komputer.visibility = View.GONE
        nilai_komputer.visibility = View.GONE


        tv_mingguke_murajaah.visibility = View.GONE
        minggukeMurajaah.visibility = View.GONE
        tv_materi_murajaah.visibility = View.GONE
        materi_murajaah.visibility = View.GONE
        tv_ket_murajaah.visibility = View.GONE
        ket_murajaah.visibility = View.GONE
        tv_nilai_murajaah.visibility = View.GONE
        nilai_murajaah.visibility = View.GONE
        submit_nilai_murajaah.visibility=View.GONE

        tv_nama_ekstra.visibility = View.GONE
        nama_ekstra.visibility = View.GONE
        tv_ket_ekstra.visibility = View.GONE
        ket_ekstra.visibility = View.GONE


        tv_laporan_perkembangan_anak.visibility = View.GONE
        perkembangan_anak.visibility = View.GONE
        tv_mingguke_perkembangan.visibility= View.GONE
        tv_angka_perkembangan.visibility=View.GONE
        minggukePerkembangan.visibility=View.GONE
        submit_nilai_perkembangan.visibility=View.GONE

        tv_saran_guru.visibility = View.GONE
        saran_guru.visibility = View.GONE
        tv_tinggi_badan.visibility = View.GONE
        tinggi_badan.visibility = View.GONE

        tv_berat_badan.visibility = View.GONE
        berat_badan.visibility = View.GONE

        tv_kondisi_kesehatan.visibility = View.GONE
        tv_penglihatan.visibility = View.GONE
        penglihatan.visibility = View.GONE
        tv_pendengaran.visibility = View.GONE
        pendengaran.visibility = View.GONE
        tv_gigi.visibility = View.GONE
        gigi.visibility = View.GONE

        tv_daya_tahan_tubuh.visibility = View.GONE
        daya_tahan.visibility = View.GONE

        tv_evaluasi_pertumbuhan_anak.visibility = View.GONE
        tv_kondisi_anak_saat_ini.visibility = View.GONE
        kondisi_saat_ini.visibility = View.GONE
        tv_kondisi_ideal.visibility = View.GONE
        kondisi_ideal.visibility = View.GONE
        tv_saran_dokter.visibility = View.GONE
        saran_dokter.visibility = View.GONE

        tv_absensi.visibility = View.GONE
        tv_izin.visibility = View.GONE
        izin.visibility = View.GONE
        tv_sakit.visibility = View.GONE
        sakit.visibility = View.GONE
        tv_tidak_ada_ket.visibility = View.GONE
        tidak_ada_keterangan.visibility = View.GONE

        tv_evaluasi_perkembangan_anak.visibility = View.GONE
        tv_kondisi_psikologi_saat_ini.visibility = View.GONE
        kondisi_psikologi_saat_ini.visibility = View.GONE
        tv_kondisi_ideal_psikologi.visibility = View.GONE
        kondisi_ideal_psikologi.visibility = View.GONE
        tv_saran_psikologi.visibility = View.GONE
        saran_psikolog.visibility = View.GONE

        tv_kondisi_okupasi_saat_ini.visibility = View.GONE
        kondisi_okupasi_saat_ini.visibility = View.GONE
        tv_kondisi_ideal_okupasi.visibility = View.GONE
        kondisi_ideal_okupasi.visibility = View.GONE
        tv_saran_okupasi.visibility = View.GONE
        saran_okupasi.visibility = View.GONE

        when(mapel){
            "Penilaian Sikap" ->{
                tv_spiritual.visibility = View.VISIBLE
                sikap_spiritual.visibility = View.VISIBLE
                tv_sosial.visibility = View.VISIBLE
                sikap_sosial.visibility = View.VISIBLE
            }
            "Kelas Pra Akademik" ->{
                tv_mingguke.visibility = View.VISIBLE
                mingguke.visibility = View.VISIBLE
                tv_materi.visibility = View.VISIBLE
                materi_sikap_sosial.visibility = View.VISIBLE
                tv_ket.visibility = View.VISIBLE
                ket_sikap_sosial.visibility = View.VISIBLE
                tv_nilai_sosial.visibility = View.VISIBLE
                nilai_sikap_sosial.visibility = View.VISIBLE
            }
            "Kelas Komputer"->{
                tv_materi_komputer.visibility = View.VISIBLE
                nilaikomputer.visibility = View.VISIBLE
                tv_ket_komputer.visibility = View.VISIBLE
                ket_komputer.visibility = View.VISIBLE
                tv_nilai_komputer.visibility = View.VISIBLE
                nilai_komputer.visibility = View.VISIBLE
            }
            "Kelas Murajaah" ->{
                tv_mingguke_murajaah.visibility = View.VISIBLE
                minggukeMurajaah.visibility = View.VISIBLE
                tv_materi_murajaah.visibility = View.VISIBLE
                materi_murajaah.visibility = View.VISIBLE
                tv_ket_murajaah.visibility = View.VISIBLE
                ket_murajaah.visibility = View.VISIBLE
                tv_nilai_murajaah.visibility = View.VISIBLE
                nilai_murajaah.visibility = View.VISIBLE
            }
            "Ekstrakulikuler" ->{
                tv_nama_ekstra.visibility = View.VISIBLE
                nama_ekstra.visibility = View.VISIBLE
                tv_ket_ekstra.visibility = View.VISIBLE
                ket_ekstra.visibility = View.VISIBLE
            }
            "Laporan Perkembangan Anak" ->{
                tv_laporan_perkembangan_anak.visibility = View.VISIBLE
                graph.visibility=View.VISIBLE
            }
            "Saran Guru" ->{
                tv_saran_guru.visibility = View.VISIBLE
                saran_guru.visibility = View.VISIBLE
            }
            "Tinggi dan Berat Badan" ->{
                tv_tinggi_badan.visibility = View.VISIBLE
                tinggi_badan.visibility = View.VISIBLE
                tv_berat_badan.visibility = View.VISIBLE
                berat_badan.visibility = View.VISIBLE
            }
            "Kondisi Kesehatan" ->{
                tv_kondisi_kesehatan.visibility = View.VISIBLE
                tv_penglihatan.visibility = View.VISIBLE
                penglihatan.visibility = View.VISIBLE
                tv_pendengaran.visibility = View.VISIBLE
                pendengaran.visibility = View.VISIBLE
                tv_gigi.visibility = View.VISIBLE
                gigi.visibility = View.VISIBLE
                tv_daya_tahan_tubuh.visibility = View.VISIBLE
                daya_tahan.visibility = View.VISIBLE
            }
            "Evaluasi Pertumbuhan Anak" ->{
                tv_evaluasi_pertumbuhan_anak.visibility = View.VISIBLE
                tv_kondisi_anak_saat_ini.visibility = View.VISIBLE
                kondisi_saat_ini.visibility = View.VISIBLE
                tv_kondisi_ideal.visibility = View.VISIBLE
                kondisi_ideal.visibility = View.VISIBLE
                tv_saran_dokter.visibility = View.VISIBLE
                saran_dokter.visibility = View.VISIBLE
            }
            "Absensi" ->{
                tv_absensi.visibility = View.VISIBLE
                tv_izin.visibility = View.VISIBLE
                izin.visibility = View.VISIBLE
                tv_sakit.visibility = View.VISIBLE
                sakit.visibility = View.VISIBLE
                tv_tidak_ada_ket.visibility = View.VISIBLE
                tidak_ada_keterangan.visibility = View.VISIBLE
            }
            "Evaluasi Perkembangan Anak" ->{
                tv_evaluasi_perkembangan_anak.visibility = View.VISIBLE
                tv_kondisi_psikologi_saat_ini.visibility = View.VISIBLE
                kondisi_psikologi_saat_ini.visibility = View.VISIBLE
                tv_kondisi_ideal_psikologi.visibility = View.VISIBLE
                kondisi_ideal_psikologi.visibility = View.VISIBLE
                tv_saran_psikologi.visibility = View.VISIBLE
                saran_psikolog.visibility = View.VISIBLE

//                tv_evaluasi_perkembangan_anak_okupasi.visibility = View.VISIBLE
                tv_kondisi_okupasi_saat_ini.visibility = View.VISIBLE
                kondisi_okupasi_saat_ini.visibility = View.VISIBLE
                tv_kondisi_ideal_okupasi.visibility = View.VISIBLE
                kondisi_ideal_okupasi.visibility = View.VISIBLE
                tv_saran_okupasi.visibility = View.VISIBLE
                saran_okupasi.visibility = View.VISIBLE
            }
            "Kelas Komunikasi Lanjutan" ->{
                daftar_komunal.visibility=View.VISIBLE
                tv_mingguke_komunal.visibility = View.VISIBLE
                mingguke_komunal.visibility = View.VISIBLE
                tv_materi_komunal.visibility = View.VISIBLE
                materi_komunal.visibility = View.VISIBLE
                tv_ket_komunal.visibility = View.VISIBLE
                ket_komunal.visibility = View.VISIBLE
                tv_nilai_komunal.visibility = View.VISIBLE
                nilai_komunal.visibility = View.VISIBLE
            }
            "Kelas Sensori Integrasi" ->{
                daftar_sensori.visibility=View.VISIBLE
                tv_mingguke_sensori.visibility = View.VISIBLE
                mingguke_sensori.visibility = View.VISIBLE
                tv_materi_sensori.visibility = View.VISIBLE
                materi_sensori.visibility = View.VISIBLE
                tv_ket_sensori.visibility = View.VISIBLE
                ket_sensori.visibility = View.VISIBLE
                tv_nilai_sensori.visibility = View.VISIBLE
                nilai_sensori.visibility = View.VISIBLE
            }
        }

    }





}

