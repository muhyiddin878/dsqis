package com.muhyiddin.dsqis.admin

import android.app.ProgressDialog
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
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.Nilai
import com.muhyiddin.dsqis.model.SavedPost
import com.muhyiddin.dsqis.model.Siswa
import kotlinx.android.synthetic.main.activity_buat_akun_fragment.*
import kotlinx.android.synthetic.main.activity_input_nilai_fragment.*
import java.util.ArrayList

class InputNilaiFragment : Fragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val data_siswa = mFirestore.collection("students")
    private lateinit var akun: MutableList<Siswa>
    private val siswa:MutableList<String> = mutableListOf()
    private val siswaId:MutableList<String> = mutableListOf()
    private lateinit var spinner:Spinner
    private lateinit var mapel:String
    private lateinit var minggu:String
    private lateinit var student:String
    private lateinit var mingguke_sikap_sosial:String
    private lateinit var minggumurajaah:String
    private lateinit var mingguke_murajaah:String
    private lateinit var idSiswa:String
    private  val arrayList = ArrayList<String>()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var cobak:String
//    private val spinner_siswa = view?.findViewById<Spinner>(R.id.daftar_siswa)


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
        val semester = view.findViewById<Spinner>(R.id.semester)
        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran)
        val mingguke_sikap_sosial_spinner= view.findViewById<Spinner>(R.id.mingguke)
        val mingguke_murajaah_spinner=view.findViewById<Spinner>(R.id.minggukeMurajaah)
        val cobak1= view.findViewById<EditText>(R.id.sikap_sosial)
        cobak = cobak1.text.toString()
        val cobak3= sikap_sosial.text.toString()






        val submit_nilai = view.findViewById<Button>(R.id.submit_nilai)


        data_siswa.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
//                akun.clear()
                for (student in querySnapshot) {
//                    akun.add(student.toObject(Siswa::class.java))
                    val nama_siswa = student.toObject(Siswa::class.java)
                    siswa.add(nama_siswa.nama)
                    siswaId.add(nama_siswa.id)
                }
                val adapter =
                    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, siswa)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.setAdapter(adapter)

            }
        }

        spinner.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                student= spinner.selectedItem.toString()
                getIdSiswa(student)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mingguke_sikap_sosial_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                minggu = mingguke_sikap_sosial_spinner.selectedItem.toString()
                if (minggu=="1"){
                    mingguke_sikap_sosial="1"
                }else if (minggu=="2"){
                    mingguke_sikap_sosial="2"
                }else if (minggu=="3"){
                    mingguke_sikap_sosial="3"
                }else if (minggu=="4"){
                    mingguke_sikap_sosial="4"
                }else if (minggu=="5"){
                    mingguke_sikap_sosial="5"
                }else if (minggu=="6"){
                    mingguke_sikap_sosial="6"
                }else if (minggu=="7"){
                    mingguke_sikap_sosial="7"
                }else if (minggu=="8"){
                    mingguke_sikap_sosial="8"
                }else if (minggu=="9"){
                    mingguke_sikap_sosial="9"
                }else if (minggu=="10"){
                    mingguke_sikap_sosial="10"
                }else if (minggu=="11"){
                    mingguke_sikap_sosial="11"
                }else if (minggu=="12"){
                    mingguke_sikap_sosial="12"
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mingguke_murajaah_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                minggumurajaah = mingguke_murajaah_spinner.selectedItem.toString()
                if (minggumurajaah=="1"){
                    mingguke_murajaah="1"
                }else if (minggumurajaah=="2"){
                    mingguke_murajaah="2"
                }else if (minggumurajaah=="3"){
                    mingguke_murajaah="3"
                }else if (minggumurajaah=="4"){
                    mingguke_murajaah="4"
                }else if (minggumurajaah=="5"){
                    mingguke_murajaah="5"
                }else if (minggumurajaah=="6"){
                    mingguke_murajaah="6"
                }else if (minggumurajaah=="7"){
                    mingguke_murajaah="7"
                }else if (minggumurajaah=="8"){
                    mingguke_murajaah="8"
                }else if (minggumurajaah=="9"){
                    mingguke_murajaah="9"
                }else if (minggumurajaah=="10"){
                    mingguke_murajaah="10"
                }else if (minggumurajaah=="11"){
                    mingguke_murajaah="11"
                }else if (minggumurajaah=="12"){
                    mingguke_murajaah="12"
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


//        arrayList.add(sikap_spiritual.text.toString())
//        arrayList.add(sikap_sosial.text.toString())



       submit_nilai.setOnClickListener {
            Log.d("ini cobak", cobak)
            Log.d("ini cobak3", cobak3)
           val nilai = Nilai()
           nilai.Penilaian_Sikap?.put("Sikap Spiritual",cobak)
           Log.d("ini nilai1", nilai.toString())
           nilai.Penilaian_Sikap?.put("Sikap Sosisal",sikap_sosial.text.toString())
           Log.d("ini nilai 2", nilai.toString())
           nilai.Kelas_Pra_Akademik?.put("Minggu Ke-",mingguke_sikap_sosial)
           nilai.Kelas_Pra_Akademik?.put("Materi",materi_sikap_sosial.text.toString())
           nilai.Kelas_Pra_Akademik?.put("Keterangan",ket_sikap_sosial.text.toString())
           nilai.Kelas_Pra_Akademik?.put("Nilai",nilai_sikap_sosial.text.toString())
           nilai.Kelas_Komputer?.put("Materi",nilaikomputer.text.toString())
           nilai.Kelas_Komputer?.put("Keterangan",ket_komputer.text.toString())
           nilai.Kelas_Komputer?.put("Nilai",nilai_komputer.text.toString())
           nilai.Kelas_Murajaah?.put("Minggu Ke",mingguke_murajaah)
           nilai.Kelas_Murajaah?.put("Materi",materi_murajaah.text.toString())
           nilai.Kelas_Murajaah?.put("Keterangan",ket_murajaah.text.toString())
           nilai.Kelas_Murajaah?.put("Nilai",nilai_murajaah.text.toString())
           nilai.Ekstrakulikuler?.put("Nama Ektra",nama_ekstra.text.toString())
           nilai.Ekstrakulikuler?.put("Keterangan",ket_ekstra.text.toString())
           nilai.Laporan_Perkembangan_Anak?.put("Perkembangan Anak",perkembangan_anak.text.toString())
           nilai.Saran_Guru?.put("Saran Guru", saran_guru.text.toString())
           nilai.TbBb?.put("Tinggi Badan",tinggi_badan.text.toString())
           nilai.TbBb?.put("Berat Badan", berat_badan.text.toString())
           nilai.Kondisi_Kesehatan?.put("Kesehatan Penglihatan",penglihatan.text.toString())
           nilai.Kondisi_Kesehatan?.put("Kesehatan Pendengaran",pendengaran.text.toString())
           nilai.Kondisi_Kesehatan?.put("Kondisi Gigi",gigi.text.toString())
           nilai.Evaluasi_Pertumbuhan_Anak?.put("Kondisi Saat Ini",kondisi_saat_ini.text.toString())
           nilai.Evaluasi_Pertumbuhan_Anak?.put("Kondisi Ideal",kondisi_ideal.text.toString())
           nilai.Evaluasi_Pertumbuhan_Anak?.put("Saran Dokter",saran_dokter.text.toString())
           nilai.Absensi?.put("Izin",izin.inputType)
           nilai.Absensi?.put("Sakit",sakit.inputType)
           nilai.Absensi?.put("Tanpa Keterangan",tidak_ada_keterangan.inputType)
           nilai.Evaluasi_Perkembangan_Anak?.put("Kondisi Psikologi Saat Ini",kondisi_psikologi_saat_ini.text.toString())
           nilai.Evaluasi_Perkembangan_Anak?.put("Kondisi Psikologi Ideal",kondisi_ideal_psikologi.text.toString())
           nilai.Evaluasi_Perkembangan_Anak?.put("Saran Psikolog",saran_psikolog.text.toString())
           nilai.Evaluasi_Perkembangan_Anak?.put("Kondisi Okupasi Saat Ini",kondisi_okupasi_saat_ini.text.toString())
           nilai.Evaluasi_Perkembangan_Anak?.put("Kondisi Okupasi Ideal",kondisi_ideal_okupasi.text.toString())
           nilai.Evaluasi_Perkembangan_Anak?.put("Saran Okupasi",saran_okupasi.text.toString())

           inputNilai(nilai)
       }



        mata_pelajaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                mapel = mata_pelajaran.selectedItem.toString()
                if(mapel=="Penilaian Sikap"){
                    tv_spiritual.visibility = View.VISIBLE
                    sikap_spiritual.visibility = View.VISIBLE
                    tv_sosial.visibility = View.VISIBLE
                    sikap_sosial.visibility = View.VISIBLE

                    tv_mingguke.visibility = View.GONE
                    mingguke.visibility = View.GONE
                    tv_materi.visibility = View.GONE
                    materi_sikap_sosial.visibility = View.GONE
                    tv_ket.visibility = View.GONE
                    ket_sikap_sosial.visibility = View.GONE
                    tv_nilai_sosial.visibility = View.GONE
                    nilai_sikap_sosial.visibility = View.GONE

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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Kelas Pra Akademik"){
                    tv_spiritual.visibility = View.GONE
                    sikap_spiritual.visibility = View.GONE
                    tv_sosial.visibility = View.GONE
                    sikap_sosial.visibility = View.GONE

                    tv_mingguke.visibility = View.VISIBLE
                    mingguke.visibility = View.VISIBLE
                    tv_materi.visibility = View.VISIBLE
                    materi_sikap_sosial.visibility = View.VISIBLE
                    tv_ket.visibility = View.VISIBLE
                    ket_sikap_sosial.visibility = View.VISIBLE
                    tv_nilai_sosial.visibility = View.VISIBLE
                    nilai_sikap_sosial.visibility = View.VISIBLE

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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Kelas Komputer"){
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

                    tv_materi_komputer.visibility = View.VISIBLE
                    nilaikomputer.visibility = View.VISIBLE
                    tv_ket_komputer.visibility = View.VISIBLE
                    ket_komputer.visibility = View.VISIBLE
                    tv_nilai_komputer.visibility = View.VISIBLE
                    nilai_komputer.visibility = View.VISIBLE


                    tv_mingguke_murajaah.visibility = View.GONE
                    minggukeMurajaah.visibility = View.GONE
                    tv_materi_murajaah.visibility = View.GONE
                    materi_murajaah.visibility = View.GONE
                    tv_ket_murajaah.visibility = View.GONE
                    ket_murajaah.visibility = View.GONE
                    tv_nilai_murajaah.visibility = View.GONE
                    nilai_murajaah.visibility = View.GONE

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Kelas Muraja'ah"){
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

                    tv_materi_komputer.visibility = View.GONE
                    nilaikomputer.visibility = View.GONE
                    tv_ket_komputer.visibility = View.GONE
                    ket_komputer.visibility = View.GONE
                    tv_nilai_komputer.visibility = View.GONE
                    nilai_komputer.visibility = View.GONE


                    tv_mingguke_murajaah.visibility = View.VISIBLE
                    minggukeMurajaah.visibility = View.VISIBLE
                    tv_materi_murajaah.visibility = View.VISIBLE
                    materi_murajaah.visibility = View.VISIBLE
                    tv_ket_murajaah.visibility = View.VISIBLE
                    ket_murajaah.visibility = View.VISIBLE
                    tv_nilai_murajaah.visibility = View.VISIBLE
                    nilai_murajaah.visibility = View.VISIBLE

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Ekstrakulikuler"){
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

                    tv_nama_ekstra.visibility = View.VISIBLE
                    nama_ekstra.visibility = View.VISIBLE
                    tv_ket_ekstra.visibility = View.VISIBLE
                    ket_ekstra.visibility = View.VISIBLE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Laporan Perkembangan Anak"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.VISIBLE
                    perkembangan_anak.visibility = View.VISIBLE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Saran Guru"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

                    tv_saran_guru.visibility = View.VISIBLE
                    saran_guru.visibility = View.VISIBLE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Tinggi dan Berat Badan"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

                    tv_saran_guru.visibility = View.GONE
                    saran_guru.visibility = View.GONE

                    tv_tinggi_badan.visibility = View.VISIBLE
                    tinggi_badan.visibility = View.VISIBLE
                    tv_berat_badan.visibility = View.VISIBLE
                    berat_badan.visibility = View.VISIBLE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Evaluasi Pertumbuhan Anak"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

                    tv_saran_guru.visibility = View.GONE
                    saran_guru.visibility = View.GONE
                    tv_tinggi_badan.visibility = View.GONE
                    tinggi_badan.visibility = View.GONE

                    tv_berat_badan.visibility = View.GONE
                    berat_badan.visibility = View.GONE

                    tv_kondisi_kesehatan.visibility = View.VISIBLE
                    tv_penglihatan.visibility = View.VISIBLE
                    penglihatan.visibility = View.VISIBLE
                    tv_pendengaran.visibility = View.VISIBLE
                    pendengaran.visibility = View.VISIBLE
                    tv_gigi.visibility = View.VISIBLE
                    gigi.visibility = View.VISIBLE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Absensi"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_absensi.visibility = View.VISIBLE
                    tv_izin.visibility = View.VISIBLE
                    izin.visibility = View.VISIBLE
                    tv_sakit.visibility = View.VISIBLE
                    sakit.visibility = View.VISIBLE
                    tv_tidak_ada_ket.visibility = View.VISIBLE
                    tidak_ada_keterangan.visibility = View.VISIBLE

                    tv_evaluasi_perkembangan_anak.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini.visibility = View.GONE
                    kondisi_psikologi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_psikologi.visibility = View.GONE
                    kondisi_ideal_psikologi.visibility = View.GONE
                    tv_saran_psikologi.visibility = View.GONE
                    saran_psikolog.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if(mapel=="Evaluasi Perkembangan Anak"){
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

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE

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

                    tv_evaluasi_perkembangan_anak.visibility = View.VISIBLE
                    tv_kondisi_psikologi_saat_ini.visibility = View.VISIBLE
                    kondisi_psikologi_saat_ini.visibility = View.VISIBLE
                    tv_kondisi_ideal_psikologi.visibility = View.VISIBLE
                    kondisi_ideal_psikologi.visibility = View.VISIBLE
                    tv_saran_psikologi.visibility = View.VISIBLE
                    saran_psikolog.visibility = View.VISIBLE

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.VISIBLE
                    tv_kondisi_okupasi_saat_ini.visibility = View.VISIBLE
                    kondisi_okupasi_saat_ini.visibility = View.VISIBLE
                    tv_kondisi_ideal_okupasi.visibility = View.VISIBLE
                    kondisi_ideal_okupasi.visibility = View.VISIBLE
                    tv_saran_okupasi.visibility = View.VISIBLE
                    saran_okupasi.visibility = View.VISIBLE

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }









    }


    private fun inputNilai(nilai:Nilai){
        mFirestore.collection("nilai")
            .document(idSiswa)
            .set(nilai)
            .addOnSuccessListener {
                Toast.makeText(context, "Nilai Berhasil Ditambahkan", Toast.LENGTH_SHORT)
                    .show()
//                for (tes2 in nilai.toString())
//                    Log.d("ini nilai", tes2.toString())
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()

            }
//        val key = firestore3.document().id
//        val nilai = Nilai(key,)

    }

    private fun getIdSiswa(namaSiswa:String){
        mFirestore.collection("students")
            .whereEqualTo("nama", namaSiswa)
            .get()
            .addOnSuccessListener {
                for (siswa in it){
                    idSiswa=siswa.id
                }

            }
    }



}
