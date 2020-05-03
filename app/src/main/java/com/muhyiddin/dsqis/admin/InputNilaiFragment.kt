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
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.R
import kotlinx.android.synthetic.main.activity_buat_akun_fragment.*
import kotlinx.android.synthetic.main.activity_input_nilai_fragment.*
import java.util.ArrayList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import android.content.ContentValues.TAG
import com.jjoe64.graphview.series.DataPoint
import com.muhyiddin.dsqis.model.*
import kotlinx.android.synthetic.main.activity_fragment_lihat_laporan.*


class InputNilaiFragment : Fragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val data_siswa = mFirestore.collection("students")
    private lateinit var akun: MutableList<Siswa>
    private val siswa: MutableList<String> = mutableListOf()
    private val siswaId: MutableList<String> = mutableListOf()
    private val grafik:MutableList<Grafik> = mutableListOf()
    private val murajaah:MutableList<Murajaah> = mutableListOf()
    private val akademik:MutableList<Murajaah> = mutableListOf()
    private lateinit var spinner: Spinner
    private lateinit var mapel: String
    private lateinit var minggu: String
    private lateinit var student: String
    private lateinit var mingguke_sikap_sosial: String
    private lateinit var minggumurajaah: String
    private lateinit var mingguke_murajaah: String
    private var mingguke_perkembangan: Int=0
    private lateinit var idSiswa: String
    private lateinit var minggukee: String
    private lateinit var mingguke_sikap_sosial_spinner:Spinner


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
        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran)
        mingguke_sikap_sosial_spinner = view.findViewById(R.id.mingguke)
        val mingguke_murajaah_spinner = view.findViewById<Spinner>(R.id.minggukeMurajaah)
        val mingguke_perkembangan_spinner = view.findViewById<Spinner>(R.id.minggukePerkembangan)
        val submit_nilai = view.findViewById<Button>(R.id.submit_nilai)
        val submit_nilai_perkembangan = view.findViewById<Button>(R.id.submit_nilai_perkembangan)
        val submit_nilai_murajaah = view.findViewById<Button>(R.id.submit_nilai_murajaah1)
        val submit_nilai_akademik = view.findViewById<Button>(R.id.submit_nilai_akademik)

        data_siswa.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                Toast.makeText(view?.context, error?.localizedMessage, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (querySnapshot != null) {
                for (student in querySnapshot) {
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



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                student = spinner.selectedItem.toString()
                getIdSiswa(student)
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
                    } else if (minggu == "2") {
                        mingguke_sikap_sosial = "2"
                    } else if (minggu == "3") {
                        mingguke_sikap_sosial = "3"
                    } else if (minggu == "4") {
                        mingguke_sikap_sosial = "4"
                    } else if (minggu == "5") {
                        mingguke_sikap_sosial = "5"
                    } else if (minggu == "6") {
                        mingguke_sikap_sosial = "6"
                    } else if (minggu == "7") {
                        mingguke_sikap_sosial = "7"
                    } else if (minggu == "8") {
                        mingguke_sikap_sosial = "8"
                    } else if (minggu == "9") {
                        mingguke_sikap_sosial = "9"
                    } else if (minggu == "10") {
                        mingguke_sikap_sosial = "10"
                    } else if (minggu == "11") {
                        mingguke_sikap_sosial = "11"
                    } else if (minggu == "12") {
                        mingguke_sikap_sosial = "12"
                    }

//                    getIdSiswa(student)

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
                    } else if (minggumurajaah == "2") {
                        mingguke_murajaah = "2"
                    } else if (minggumurajaah == "3") {
                        mingguke_murajaah = "3"
                    } else if (minggumurajaah == "4") {
                        mingguke_murajaah = "4"
                    } else if (minggumurajaah == "5") {
                        mingguke_murajaah = "5"
                    } else if (minggumurajaah == "6") {
                        mingguke_murajaah = "6"
                    } else if (minggumurajaah == "7") {
                        mingguke_murajaah = "7"
                    } else if (minggumurajaah == "8") {
                        mingguke_murajaah = "8"
                    } else if (minggumurajaah == "9") {
                        mingguke_murajaah = "9"
                    } else if (minggumurajaah == "10") {
                        mingguke_murajaah = "10"
                    } else if (minggumurajaah == "11") {
                        mingguke_murajaah = "11"
                    } else if (minggumurajaah == "12") {
                        mingguke_murajaah = "12"
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
                    } else if (minggu == "2") {
                        mingguke_perkembangan = 2
                    } else if (minggu == "3") {
                        mingguke_perkembangan = 3
                    } else if (minggu == "4") {
                        mingguke_perkembangan = 4
                    } else if (minggu == "5") {
                        mingguke_perkembangan = 5
                    } else if (minggu == "6") {
                        mingguke_perkembangan = 6
                    } else if (minggu == "7") {
                        mingguke_perkembangan = 7
                    } else if (minggu == "8") {
                        mingguke_perkembangan = 8
                    } else if (minggu == "9") {
                        mingguke_perkembangan = 9
                    } else if (minggu == "10") {
                        mingguke_perkembangan = 10
                    } else if (minggu == "11") {
                        mingguke_perkembangan = 11
                    } else if (minggu == "12") {
                        mingguke_perkembangan = 12
                    }
//                    getIdSiswa(student)

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

        submit_nilai_perkembangan.setOnClickListener {
            submit_perkembangan()
        }
        submit_nilai_murajaah.setOnClickListener {
            submit_murajaah1()
        }
        submit_nilai_akademik.setOnClickListener{
            submit_akademik()
        }

        submit_nilai.setOnClickListener {

            val Penilaian_Sikap = HashMap<String, String>()
            Penilaian_Sikap.put("Sikap Spiritual", sikap_spiritual.text.toString())
            Penilaian_Sikap.put("Sikap Sosisal", sikap_sosial.text.toString())

            val Kelas_Komputer = HashMap<String, Any>()
            Kelas_Komputer.put("Materi", nilaikomputer.text.toString())
            Kelas_Komputer.put("Keterangan", ket_komputer.text.toString())
            Kelas_Komputer.put("Nilai", nilai_komputer.text.toString())


            val Ekstrakulikuler = HashMap<String, String>()
            Ekstrakulikuler.put("Nama Ektra", nama_ekstra.text.toString())
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
            Absensi.put("Izin", izin.inputType)
            Absensi.put("Sakit", sakit.inputType)
            Absensi.put("Tanpa Keterangan", tidak_ada_keterangan.inputType)

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

            val nilai = Nilai()

            nilai.Penilaian_Sikap = Penilaian_Sikap
            nilai.Kelas_Komputer = Kelas_Komputer
            nilai.Ekstrakulikuler = Ekstrakulikuler
            nilai.Saran_Guru = Saran_Guru
            nilai.TbBb = TbBb
            nilai.Kondisi_Kesehatan = Kondisi_Kesehatan
            nilai.Evaluasi_Pertumbuhan_Anak = Evaluasi_Pertumbuhan_Anak
            nilai.Absensi = Absensi
            nilai.Evaluasi_Perkembangan_Anak = Evaluasi_Perkembangan_Anak

           inputNilai(nilai)
        }



        mata_pelajaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                mapel = mata_pelajaran.selectedItem.toString()
                if (mapel == "Penilaian Sikap") {
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
                    submit_nilai_akademik.visibility=View.GONE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if (mapel == "Kelas Pra Akademik") {
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
                    submit_nilai_akademik.visibility=View.VISIBLE

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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if (mapel == "Kelas Komputer") {
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
                    submit_nilai_murajaah.visibility=View.GONE

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE
                    tv_mingguke_perkembangan.visibility= View.GONE
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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if (mapel == "Kelas Muraja'ah") {
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
                    submit_nilai_murajaah.visibility=View.VISIBLE

                    tv_nama_ekstra.visibility = View.GONE
                    nama_ekstra.visibility = View.GONE
                    tv_ket_ekstra.visibility = View.GONE
                    ket_ekstra.visibility = View.GONE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE
                    tv_mingguke_perkembangan.visibility= View.GONE
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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if (mapel == "Ekstrakulikuler") {
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

                    tv_nama_ekstra.visibility = View.VISIBLE
                    nama_ekstra.visibility = View.VISIBLE
                    tv_ket_ekstra.visibility = View.VISIBLE
                    ket_ekstra.visibility = View.VISIBLE


                    tv_laporan_perkembangan_anak.visibility = View.GONE
                    perkembangan_anak.visibility = View.GONE
                    tv_mingguke_perkembangan.visibility= View.GONE
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

                    tv_evaluasi_perkembangan_anak_okupasi.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini.visibility = View.GONE
                    kondisi_okupasi_saat_ini.visibility = View.GONE
                    tv_kondisi_ideal_okupasi.visibility = View.GONE
                    kondisi_ideal_okupasi.visibility = View.GONE
                    tv_saran_okupasi.visibility = View.GONE
                    saran_okupasi.visibility = View.GONE

                }
                if (mapel == "Laporan Perkembangan Anak") {
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


                    tv_laporan_perkembangan_anak.visibility = View.VISIBLE
                    perkembangan_anak.visibility = View.VISIBLE
                    tv_mingguke_perkembangan.visibility= View.VISIBLE
                    minggukePerkembangan.visibility=View.VISIBLE
                    submit_nilai_perkembangan.visibility=View.VISIBLE

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
                if (mapel == "Saran Guru") {
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
                    minggukePerkembangan.visibility=View.GONE
                    submit_nilai_perkembangan.visibility=View.GONE

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
                if (mapel == "Tinggi dan Berat Badan") {
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
                    minggukePerkembangan.visibility=View.GONE
                    submit_nilai_perkembangan.visibility=View.GONE

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
                if (mapel == "Kondisi Kesehatan") {
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
                    minggukePerkembangan.visibility=View.GONE
                    submit_nilai_perkembangan.visibility=View.GONE

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
                    tv_daya_tahan_tubuh.visibility = View.VISIBLE
                    daya_tahan.visibility = View.VISIBLE

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
                if (mapel == "Evaluasi Pertumbuhan Anak") {
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

                    tv_evaluasi_pertumbuhan_anak.visibility = View.VISIBLE
                    tv_kondisi_anak_saat_ini.visibility = View.VISIBLE
                    kondisi_saat_ini.visibility = View.VISIBLE
                    tv_kondisi_ideal.visibility = View.VISIBLE
                    kondisi_ideal.visibility = View.VISIBLE
                    tv_saran_dokter.visibility = View.VISIBLE
                    saran_dokter.visibility = View.VISIBLE

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
                if (mapel == "Absensi") {
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
                if (mapel == "Evaluasi Perkembangan Anak") {
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


    private fun inputNilai(nilai: Nilai) {
        mFirestore.collection("nilai")
            .document(idSiswa)
            .set(nilai)
            .addOnSuccessListener {
                Toast.makeText(context, "Nilai Berhasil Ditambahkan", Toast.LENGTH_SHORT)
                    .show()

            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()

            }

    }
    private fun submit_perkembangan(){
        mFirestore.collection("nilai")
            .document(idSiswa)
            .get()
            .addOnSuccessListener {
                val isi = it.toObject(Nilai::class.java)
                grafik.clear()
                if (isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")!=null){
                    val graph1= isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak") as List<Grafik>
                    grafik.addAll(graph1)

                }
                val graphic = Grafik(perkembangan_anak.text.toString().toInt(),mingguke_perkembangan)
                grafik.add(graphic)

                val Laporan_Perkembangan_Anak = HashMap<String, MutableList<Grafik>>()
                Laporan_Perkembangan_Anak.put("Perkembangan Anak", grafik)
                val nilai = Nilai()
                nilai.Laporan_Perkembangan_Anak = Laporan_Perkembangan_Anak
                inputNilai(nilai)

            }

    }

    private fun submit_murajaah1(){
        mFirestore.collection("nilai")
            .document(idSiswa)
            .get()
            .addOnSuccessListener {
                val isi = it.toObject(Nilai::class.java)
                murajaah.clear()
                if (isi?.Kelas_Murajaah?.get("Kelas Murajaah")!=null){
                    val murajaah1= isi?.Kelas_Murajaah?.get("Kelas Murajaah") as List<Murajaah>
                    murajaah.addAll(murajaah1)
                }

                val murajaah2 = Murajaah(mingguke_murajaah,materi_murajaah.text.toString(),ket_murajaah.text.toString(),nilai_murajaah.text.toString().toInt())
                murajaah.add(murajaah2)

                val Kelas_Murajaah = HashMap<String, MutableList<Murajaah>>()
                Kelas_Murajaah.put("Kelas Murajaah", murajaah)
                val nilai = Nilai()
                nilai.Kelas_Murajaah = Kelas_Murajaah
                inputNilai(nilai)

            }

    }

    private fun submit_akademik(){
        mFirestore.collection("nilai")
            .document(idSiswa)
            .get()
            .addOnSuccessListener {
                val isi = it.toObject(Nilai::class.java)
                akademik.clear()
                if (isi?.Kelas_Pra_Akademik?.get("Penilaian Kelas Pra Akademik")!=null){
                    val akademik1= isi?.Kelas_Pra_Akademik?.get("Penilaian Kelas Pra Akademik") as List<Murajaah>
                    akademik.addAll(akademik1)
                }

                val akademik2 = Murajaah(mingguke_sikap_sosial,materi_sikap_sosial.text.toString(),ket_sikap_sosial.text.toString(),nilai_sikap_sosial.text.toString().toInt())
                akademik.add(akademik2)

                val Kelas_Pra_Akademik = HashMap<String, MutableList<Murajaah>>()
                Kelas_Pra_Akademik.put("Kelas Pra Akademik", akademik)
                val nilai = Nilai()
                nilai.Kelas_Pra_Akademik= Kelas_Pra_Akademik
                inputNilai(nilai)

            }

    }


    private fun getIdSiswa(namaSiswa: String) {
        mFirestore.collection("students")
            .whereEqualTo("nama", namaSiswa)
            .get()
            .addOnSuccessListener {
                for (siswa in it) {
                    idSiswa = siswa.id
                    getLatestNilai(idSiswa)
//                    setMinggu(idSiswa)

                }

            }
    }

    private fun setMinggu(id:String){
        if (id!=null){
        val arrayMinggu = resources.getStringArray(R.array.minggu)
        val pos = arrayMinggu.indexOf(minggukee)
        mingguke_sikap_sosial_spinner.setSelection(pos)
        }
    }

    private fun getLatestNilai(siswaId:String) {
        mFirestore.collection("nilai")
            .document(siswaId)
            .get()
            .addOnSuccessListener {
                val isi=it.toObject(Nilai::class.java)


                if (isi?.Kelas_Pra_Akademik?.get("Kelas Pra Akademik") != null) {
                    val akademik1 = isi?.Kelas_Pra_Akademik?.get("Kelas Pra Akademik")
                    for (i in akademik1!!.indices) {
                        if (mingguke_sikap_sosial == akademik1[i].minggu) {
                            materi_sikap_sosial.setText(akademik1[i].materi)
                            ket_sikap_sosial.setText(akademik1[i].keterangan)
                            nilai_sikap_sosial.setText(akademik1[i].nilai.toString())
                            break
                        }
                    }
                }
                else if (isi?.Penilaian_Sikap?.get("Sikap Spiritual") !=null) {
                    sikap_spiritual.setText("${isi?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                }else if(isi?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                    sikap_sosial.setText("${isi?.Penilaian_Sikap?.get("Sikap Sosial")}")
                }else if(isi?.Kelas_Komputer?.get("Materi")!=null){
                    nilaikomputer.setText("${isi?.Kelas_Komputer?.get("Materi")}")
                }else if(isi?.Kelas_Komputer?.get("Keterangan")!=null){
                    ket_komputer.setText("${isi?.Kelas_Komputer?.get("Keterangan")}")
                }else if(isi?.Kelas_Komputer?.get("Nilai")!=null){
                    nilai_komputer.setText("${isi?.Kelas_Komputer?.get("Nilai")}")
                }

                else if (isi?.Kelas_Murajaah?.get("Kelas Murajaah") != null) {
                    val murajaah1 = isi?.Kelas_Murajaah?.get("Kelas Murajaah")
                    for (i in murajaah1!!.indices) {
                        if (mingguke_murajaah == murajaah1[i].minggu) {
                            materi_murajaah.setText(murajaah1[i].materi)
                            ket_murajaah.setText(murajaah1[i].keterangan)
                            nilai_murajaah.setText(murajaah1[i].nilai.toString())
                            break
                        }
                    }

                }


                else if (isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak") != null) {
                    val laporan1 = isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")
                    for (i in laporan1!!.indices) {
                        if (mingguke_perkembangan == laporan1[i].minggu) {
                            perkembangan_anak.setText(laporan1[i].angka!!.toInt())
                            break
                        }
                    }

                }
                else if(isi?.Ekstrakulikuler?.get("Nama Ektra")!=null){
                    nama_ekstra.setText("${isi?.Ekstrakulikuler?.get("Nama Ektra")}")
                }else if(isi?.Ekstrakulikuler?.get("Keterangan")!=null){
                    ket_ekstra.setText("${isi?.Ekstrakulikuler?.get("Keterangan")}")
                }else if (isi?.Saran_Guru?.get("Saran Guru")!=null){
                    saran_guru.setText("${isi?.Saran_Guru?.get("Saran Guru")}")
                }else if(isi?.TbBb?.get("Tinggi Badan")!=null){
                    tinggi_badan.setText("${isi?.TbBb?.get("Tinggi Badan")}")
                }else if (isi?.TbBb?.get("Berat Badan")!=null){
                    berat_badan.setText("${isi?.TbBb?.get("Berat Badan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                    penglihatan.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                    pendengaran.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                }else if (isi?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                    daya_tahan.setText("${isi?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                    gigi.setText("${isi?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                    kondisi_saat_ini.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                    kondisi_ideal.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                    saran_dokter.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                }else if(isi?.Absensi?.get("Izin")!= null){
                    izin.setText("${isi?.Absensi?.get("Izin")}")
                }else if(isi?.Absensi?.get("Sakit")!=null){
                    sakit.setText("${isi?.Absensi?.get("Sakit")}")
                }else if (isi?.Absensi?.get("Tanpa Keterangan")!=null){
                    tidak_ada_keterangan.setText("${isi?.Absensi?.get("Tanpa Keterangan")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                    kondisi_psikologi_saat_ini.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                    kondisi_ideal_psikologi.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                    saran_psikolog.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                    kondisi_okupasi_saat_ini.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                    kondisi_ideal_okupasi.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                    saran_okupasi.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                }
//                mingguke
//                mingguke_murajaah
            }


        }



}

