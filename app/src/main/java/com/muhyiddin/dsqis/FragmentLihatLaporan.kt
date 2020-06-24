package com.muhyiddin.dsqis

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.adapter.ListSiswaAdapter
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_fragment_lihat_laporan.*
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import com.muhyiddin.dsqis.model.*
import kotlinx.android.synthetic.main.activity_input_nilai_fragment.*
import java.util.*


class FragmentLihatLaporan : Fragment() {
    private lateinit var mapel: String
    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var idSiswa: String
    lateinit var prefs: AppPreferences
    private val CHOOSE_IMAGE = 101
    var resolver: ContentResolver?=null
    private var siswaId: String? = null
    private lateinit var minggumurajaah: String
    private lateinit var mingguke_murajaah: String
    private lateinit var mingguke_murajaah_spinner:Spinner
    private lateinit var minggu: String
    private lateinit var mingguke_sikap_sosial: String
    private lateinit var mingguke_sikap_sosial_spinner:Spinner

    private var Kelas_Murajaah = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Pra_Akademik = HashMap<String, MutableList<Murajaah>>()
    private var Laporan_Perkembangan_Anak = HashMap<String, MutableList<Grafik>>()
    private val listTanggal = mutableListOf<String>()
    private val listNilai = mutableListOf<Nilai>()

    private val listMurajaah = mutableListOf<Murajaah>()
    private val listPraAkademik = mutableListOf<Murajaah>()
    private val listPerkembangan = mutableListOf<Grafik>()
    private var nilai: Nilai? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "Laporan Anak"
        return inflater.inflate(R.layout.activity_fragment_lihat_laporan, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran1)
        prefs=AppPreferences(context)

        getIdFromParent()
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()

        mingguke_murajaah_spinner = view.findViewById(R.id.minggukeMurajaah1)
        mingguke_sikap_sosial_spinner = view.findViewById(R.id.mingguke1)

        mingguke_sikap_sosial_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                    minggu = mingguke_sikap_sosial_spinner.selectedItem.toString()
                    if (minggu == "1") {
                        mingguke_sikap_sosial = "1"
                        nilai_sikap_sosial1.setText("")
                        ket_sikap_sosial1.setText("")
                        materi_sikap_sosial1.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "2") {
                        mingguke_sikap_sosial = "2"
                        nilai_sikap_sosial1.setText("")
                        ket_sikap_sosial1.setText("")
                        materi_sikap_sosial1.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "3") {
                        mingguke_sikap_sosial = "3"
                        nilai_sikap_sosial1.setText("")
                        ket_sikap_sosial1.setText("")
                        materi_sikap_sosial1.setText("")
                        setPraAkademikValue(mingguke_sikap_sosial)
                    } else if (minggu == "4") {
                        mingguke_sikap_sosial = "4"
                        nilai_sikap_sosial1.setText("")
                        ket_sikap_sosial1.setText("")
                        materi_sikap_sosial1.setText("")
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
                        nilai_murajaah1.setText("")
                        ket_murajaah1.setText("")
                        materi_murajaah1.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "2") {
                        mingguke_murajaah = "2"
                        nilai_murajaah1.setText("")
                        ket_murajaah1.setText("")
                        materi_murajaah1.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "3") {
                        mingguke_murajaah = "3"
                        nilai_murajaah1.setText("")
                        ket_murajaah1.setText("")
                        materi_murajaah1.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    } else if (minggumurajaah == "4") {
                        mingguke_murajaah = "4"
                        nilai_murajaah1.setText("")
                        ket_murajaah1.setText("")
                        materi_murajaah1.setText("")
                        setMurajaahValue(mingguke_murajaah)
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }



        tanggal_nilai1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (tanggal_nilai1.selectedItemPosition==0) {
                    sikap_spiritual1.setText("")
                    sikap_sosial1.setText("")

                    nilaikomputer1.setText("")
                    ket_komputer1.setText("")
                    nilai_komputer1.setText("")

                    nama_ekstra1.setText("")
                    ket_ekstra1.setText("")

                    saran_guru1.setText("")

                    tinggi_badan1.setText("")
                    berat_badan1.setText("")

                    penglihatan1.setText("")
                    pendengaran1.setText("")
                    daya_tahan1.setText("")
                    gigi1.setText("")

                    kondisi_saat_ini1.setText("")
                    kondisi_ideal1.setText("")
                    saran_dokter1.setText("")

                    izin1.setText("")
                    sakit1.setText("")
                    tidak_ada_keterangan1.setText("")

                    kondisi_psikologi_saat_ini1.setText("")
                    kondisi_ideal_psikologi1.setText("")
                    saran_psikolog1.setText("")
                    kondisi_okupasi_saat_ini1.setText("")
                    tv_kondisi_ideal_okupasi1.setText("")
                    saran_okupasi1.setText("")

                } else {
                    nilai = listNilai.find {
                        it.tanggal == tanggal_nilai1.selectedItem
                    }
                    siswaId = idSiswa

                    when(mata_pelajaran.selectedItem) {
                        "Penilaian Sikap" -> {
                            if (nilai?.Penilaian_Sikap?.get("Sikap Spiritual")!=null){
                                sikap_spiritual1.setText("${nilai?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                            }
                            if(nilai?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                                sikap_sosial1.setText("${nilai?.Penilaian_Sikap?.get("Sikap Sosial")}")
                            }
                        }
                        "Kelas Pra Akademik" -> {
                            if(nilai?.Kelas_Pra_Akademik!=null){
                                Log.d("Pra Akademik", "${nilai?.Kelas_Pra_Akademik}")
                                Kelas_Pra_Akademik = nilai?.Kelas_Pra_Akademik!!
                                Kelas_Pra_Akademik?.get("Kelas Pra Akademik")?.forEach {
                                    listPraAkademik.add(it)
                                }
                                Log.d("LIST_MURAJAAH", "$listPraAkademik")
                            }

                        }
                        "Kelas Komputer" -> {
                            if(nilai?.Kelas_Komputer?.get("Materi")!=null){
                                nilaikomputer1.setText("${nilai?.Kelas_Komputer?.get("Materi")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Keterangan")!=null){
                                ket_komputer1.setText("${nilai?.Kelas_Komputer?.get("Keterangan")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Nilai")!=null){
                                nilai_komputer1.setText("${nilai?.Kelas_Komputer?.get("Nilai")}")
                            }

                        }
                        "Kelas Muraja'ah" -> {
                            if(nilai?.Kelas_Murajaah!=null){
                                Log.d("MURAJAAH", "${nilai?.Kelas_Murajaah}")
                                Kelas_Murajaah = nilai?.Kelas_Murajaah!!
                                Kelas_Murajaah?.get("Kelas Murajaah")?.forEach {
                                    listMurajaah.add(it)
                                }
                                Log.d("LIST_MURAJAAH", "$listMurajaah")
                            }

                        }
                        "Ekstrakulikuler" -> {
                            if(nilai?.Ekstrakulikuler?.get("Nama Ekstra")!=null){
                                nama_ekstra1.setText("${nilai?.Ekstrakulikuler?.get("Nama Ekstra")}")
                            }
                            if(nilai?.Ekstrakulikuler?.get("Keterangan")!=null){
                                ket_ekstra1.setText("${nilai?.Ekstrakulikuler?.get("Keterangan")}")
                            }

                        }
                        "Laporan Perkembangan Anak" -> {

                            if (nilai?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")!=null){
                                val graph1= nilai?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")
                                Log.d("TES ISI graph1", "$graph1")
                                val dataPoints =arrayOfNulls<DataPoint>(graph1!!.size)
                                for (i in graph1.indices) {
                                    dataPoints[i] = DataPoint(
                                        graph1[i].angka!!.plus(0.0),
                                        graph1[i].minggu!!.plus(0.0)
                                    )
                                }
                                val series = LineGraphSeries<DataPoint>(dataPoints)
                                val series2 = PointsGraphSeries<DataPoint>(dataPoints)
                                graph.addSeries(series)
                                graph.addSeries(series2)
                                series2.setShape(PointsGraphSeries.Shape.POINT)
                                series.setTitle("Perkembangan Anak")
                                graph.getLegendRenderer().setVisible(true)
                                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP)

                            }
                            if(nilai?.Laporan_Perkembangan_Anak!=null){
                                Log.d("Perkembangan", "${nilai?.Laporan_Perkembangan_Anak}")
                                Laporan_Perkembangan_Anak = nilai?.Laporan_Perkembangan_Anak!!
                                Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")?.forEach {
                                    listPerkembangan.add(it)
                                }
                            }

                        }
                        "Saran Guru" -> {
                            if(nilai?.Saran_Guru?.get("Saran Guru")!=null){
                                saran_guru1.setText("${nilai?.Saran_Guru?.get("Saran Guru")}")
                            }

                        }
                        "Tinggi dan Berat Badan" -> {
                            if(nilai?.TbBb?.get("Tinggi Badan")!=null){
                                tinggi_badan1.setText("${nilai?.TbBb?.get("Tinggi Badan")}")
                            }
                            if(nilai?.TbBb?.get("Berat Badan")!=null){
                                berat_badan1.setText("${nilai?.TbBb?.get("Berat Badan")}")
                            }

                        }
                        "Kondisi Kesehatan" -> {
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                                penglihatan1.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                                pendengaran1.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                                daya_tahan1.setText("${nilai?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                                gigi1.setText("${nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                            }

                        }
                        "Evaluasi Pertumbuhan Anak" -> {
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                                kondisi_saat_ini1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                                kondisi_ideal1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                                saran_dokter1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                            }

                        }
                        "Absensi" -> {

                            if(nilai?.Absensi?.get("Izin")!=null){
                                izin1.setText("${nilai?.Absensi?.get("Izin")}")
                            }
                            if(nilai?.Absensi?.get("Sakit")!=null){
                                sakit1.setText("${nilai?.Absensi?.get("Sakit")}")
                            }
                            if(nilai?.Absensi?.get("Tanpa Keterangan")!=null){
                                tidak_ada_keterangan1.setText("${nilai?.Absensi?.get("Tanpa Keterangan")}")
                            }

                        }
                        "Evaluasi Perkembangan Anak" -> {
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                                kondisi_psikologi_saat_ini1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                                kondisi_ideal_psikologi1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                                saran_psikolog1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                                kondisi_okupasi_saat_ini1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                                kondisi_ideal_okupasi1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                                saran_okupasi1.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                            }
                        }

                    }
                }


            }

        }





        mata_pelajaran.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, l: Long) {
                mapel = mata_pelajaran.selectedItem.toString()
                if (mapel == "Penilaian Sikap") {
                    tv_spiritual1.visibility = View.VISIBLE
                    sikap_spiritual1.visibility = View.VISIBLE
                    tv_sosial1.visibility = View.VISIBLE
                    sikap_sosial1.visibility = View.VISIBLE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Kelas Pra Akademik") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.VISIBLE
                    mingguke1.visibility = View.VISIBLE
                    tv_materi1.visibility = View.VISIBLE
                    materi_sikap_sosial1.visibility = View.VISIBLE
                    tv_ket1.visibility = View.VISIBLE
                    ket_sikap_sosial1.visibility = View.VISIBLE
                    tv_nilai_sosial1.visibility = View.VISIBLE
                    nilai_sikap_sosial1.visibility = View.VISIBLE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Kelas Komputer") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.VISIBLE
                    nilaikomputer1.visibility = View.VISIBLE
                    tv_ket_komputer1.visibility = View.VISIBLE
                    ket_komputer1.visibility = View.VISIBLE
                    tv_nilai_komputer1.visibility = View.VISIBLE
                    nilai_komputer1.visibility = View.VISIBLE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Kelas Muraja'ah") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.VISIBLE
                    minggukeMurajaah1.visibility = View.VISIBLE
                    tv_materi_murajaah1.visibility = View.VISIBLE
                    materi_murajaah1.visibility = View.VISIBLE
                    tv_ket_murajaah1.visibility = View.VISIBLE
                    ket_murajaah1.visibility = View.VISIBLE
                    tv_nilai_murajaah1.visibility = View.VISIBLE
                    nilai_murajaah1.visibility = View.VISIBLE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Ekstrakulikuler") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.VISIBLE
                    nama_ekstra1.visibility = View.VISIBLE
                    tv_ket_ekstra1.visibility = View.VISIBLE
                    ket_ekstra1.visibility = View.VISIBLE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Laporan Perkembangan Anak") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.VISIBLE
//                    perkembangan_anak1.visibility = View.VISIBLE
                    graph.visibility=View.VISIBLE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE
                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE

                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Saran Guru") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.VISIBLE
                    saran_guru1.visibility = View.VISIBLE

                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE
                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE

                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Tinggi dan Berat Badan") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE

                    tv_tinggi_badan1.visibility = View.VISIBLE
                    tinggi_badan1.visibility = View.VISIBLE
                    tv_berat_badan1.visibility = View.VISIBLE
                    berat_badan1.visibility = View.VISIBLE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE
                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Kondisi Kesehatan") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE

                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE
                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.VISIBLE
                    tv_penglihatan1.visibility = View.VISIBLE
                    penglihatan1.visibility = View.VISIBLE
                    tv_pendengaran1.visibility = View.VISIBLE
                    pendengaran1.visibility = View.VISIBLE
                    tv_gigi1.visibility = View.VISIBLE
                    gigi1.visibility = View.VISIBLE
                    tv_daya_tahan_tubuh1.visibility = View.VISIBLE
                    daya_tahan1.visibility = View.VISIBLE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Evaluasi Pertumbuhan Anak") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE

                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE
                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE
                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.VISIBLE
                    tv_kondisi_anak_saat_ini1.visibility = View.VISIBLE
                    kondisi_saat_ini1.visibility = View.VISIBLE
                    tv_kondisi_ideal1.visibility = View.VISIBLE
                    kondisi_ideal1.visibility = View.VISIBLE
                    tv_saran_dokter1.visibility = View.VISIBLE
                    saran_dokter1.visibility = View.VISIBLE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE


                }
                if (mapel == "Absensi") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE

                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE
                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE
                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.VISIBLE
                    tv_izin1.visibility = View.VISIBLE
                    izin1.visibility = View.VISIBLE
                    tv_sakit1.visibility = View.VISIBLE
                    sakit1.visibility = View.VISIBLE
                    tv_tidak_ada_ket1.visibility = View.VISIBLE
                    tidak_ada_keterangan1.visibility = View.VISIBLE

                    tv_evaluasi_perkembangan_anak1.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.GONE
                    kondisi_psikologi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_psikologi1.visibility = View.GONE
                    kondisi_ideal_psikologi1.visibility = View.GONE
                    tv_saran_psikologi1.visibility = View.GONE
                    saran_psikolog1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }
                if (mapel == "Evaluasi Perkembangan Anak") {
                    tv_spiritual1.visibility = View.GONE
                    sikap_spiritual1.visibility = View.GONE
                    tv_sosial1.visibility = View.GONE
                    sikap_sosial1.visibility = View.GONE

                    tv_mingguke1.visibility = View.GONE
                    mingguke1.visibility = View.GONE
                    tv_materi1.visibility = View.GONE
                    materi_sikap_sosial1.visibility = View.GONE
                    tv_ket1.visibility = View.GONE
                    ket_sikap_sosial1.visibility = View.GONE
                    tv_nilai_sosial1.visibility = View.GONE
                    nilai_sikap_sosial1.visibility = View.GONE

                    tv_materi_komputer1.visibility = View.GONE
                    nilaikomputer1.visibility = View.GONE
                    tv_ket_komputer1.visibility = View.GONE
                    ket_komputer1.visibility = View.GONE
                    tv_nilai_komputer1.visibility = View.GONE
                    nilai_komputer1.visibility = View.GONE


                    tv_mingguke_murajaah1.visibility = View.GONE
                    minggukeMurajaah1.visibility = View.GONE
                    tv_materi_murajaah1.visibility = View.GONE
                    materi_murajaah1.visibility = View.GONE
                    tv_ket_murajaah1.visibility = View.GONE
                    ket_murajaah1.visibility = View.GONE
                    tv_nilai_murajaah1.visibility = View.GONE
                    nilai_murajaah1.visibility = View.GONE

                    tv_nama_ekstra1.visibility = View.GONE
                    nama_ekstra1.visibility = View.GONE
                    tv_ket_ekstra1.visibility = View.GONE
                    ket_ekstra1.visibility = View.GONE


                    tv_laporan_perkembangan_anak1.visibility = View.GONE
//                    perkembangan_anak1.visibility = View.GONE
                    graph.visibility=View.GONE

                    tv_saran_guru1.visibility = View.GONE
                    saran_guru1.visibility = View.GONE

                    tv_tinggi_badan1.visibility = View.GONE
                    tinggi_badan1.visibility = View.GONE
                    tv_berat_badan1.visibility = View.GONE
                    berat_badan1.visibility = View.GONE

                    tv_kondisi_kesehatan1.visibility = View.GONE
                    tv_penglihatan1.visibility = View.GONE
                    penglihatan1.visibility = View.GONE
                    tv_pendengaran1.visibility = View.GONE
                    pendengaran1.visibility = View.GONE
                    tv_gigi1.visibility = View.GONE
                    gigi1.visibility = View.GONE
                    tv_daya_tahan_tubuh1.visibility = View.GONE
                    daya_tahan1.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak1.visibility = View.GONE
                    tv_kondisi_anak_saat_ini1.visibility = View.GONE
                    kondisi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal1.visibility = View.GONE
                    kondisi_ideal1.visibility = View.GONE
                    tv_saran_dokter1.visibility = View.GONE
                    saran_dokter1.visibility = View.GONE

                    tv_absensi1.visibility = View.GONE
                    tv_izin1.visibility = View.GONE
                    izin1.visibility = View.GONE
                    tv_sakit1.visibility = View.GONE
                    sakit1.visibility = View.GONE
                    tv_tidak_ada_ket1.visibility = View.GONE
                    tidak_ada_keterangan1.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak1.visibility = View.VISIBLE
                    tv_kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
                    kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
                    tv_kondisi_ideal_psikologi1.visibility = View.VISIBLE
                    kondisi_ideal_psikologi1.visibility = View.VISIBLE
                    tv_saran_psikologi1.visibility = View.VISIBLE
                    saran_psikolog1.visibility = View.VISIBLE

                    tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
                    kondisi_okupasi_saat_ini1.visibility = View.GONE
                    tv_kondisi_ideal_okupasi1.visibility = View.GONE
                    kondisi_ideal_okupasi1.visibility = View.GONE
                    tv_saran_okupasi1.visibility = View.GONE
                    saran_okupasi1.visibility = View.GONE

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }




    }

    private fun setMurajaahValue(minggukeMurajaah: String) {
        val murajaah = listMurajaah.find {it.minggu == minggukeMurajaah}
        if (murajaah!=null) {
            val nilaii=murajaah.nilai
            nilai_murajaah1.setText(Integer.toString(nilaii!!))
            ket_murajaah1.setText(murajaah.keterangan)
            materi_murajaah1.setText(murajaah.materi)
        }
    }

    private fun setPraAkademikValue(minggukePraAkademik:String){
        val praAkademik = listPraAkademik.find { it.minggu==minggukePraAkademik }
        if(praAkademik!=null){
            val nilai=praAkademik.nilai
            materi_sikap_sosial1.setText(praAkademik.materi)
            ket_sikap_sosial1.setText(praAkademik.keterangan)
            nilai_sikap_sosial1.setText(Integer.toString((nilai!!)))

        }
    }

//    fun getCurrentDate():String{
//        val date = Calendar.getInstance().get(Calendar.DATE)
//        val month = Calendar.getInstance().get(Calendar.MONTH)+1
//        val year = Calendar.getInstance().get(Calendar.YEAR)
//        when(month){
//            1 -> return "$date Januari $year"
//            2 -> return "$date Februari $year"
//            3 -> return "$date Maret $year"
//            4 -> return "$date April $year"
//            5 -> return "$date Mei $year"
//            6 -> return "$date Juni $year"
//            7 -> return "$date Juli $year"
//            8 -> return "$date Agustus $year"
//            9 -> return "$date September $year"
//            10 -> return "$date Oktober $year"
//            11 -> return "$date November $year"
//            12 -> return "$date Desember $year"
//            else -> return month.toString()
//        }
//    }




    private fun getIdFromParent() {
        Log.d("MASUK","MASUKKKK")
        val id =prefs.uid
        val ref= mFirestore.collection("parents")
            ref.document(id).collection("students")
            .get()
            .addOnSuccessListener {
                Log.d("first it", it.first().id)
                val sis =it.first().toObject(Siswa::class.java)
                idSiswa= sis.id
//                it.forEach {sis->
//                    Log.d("testing", sis.id)
//                    val datasiswa = sis.toObject(Siswa::class.java)
//                    idSiswa = datasiswa.id
//                    Log.d("ID",idSiswa)
//                }

                getDataSiswa(idSiswa)
                getNilai(idSiswa)

            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }




    }

    fun getNilai(idSiswa: String){

        listTanggal.clear()
        listTanggal.add("Tanggal")
        var ref = mFirestore.collection("nilai")
        ref.whereEqualTo("idSiswa", idSiswa).get()
            .addOnSuccessListener {
                listNilai.clear()
                it.forEach {
                    val nilai = it.toObject(Nilai::class.java)
                    listNilai.add(nilai)
                }
                Log.d("ISI NILAIII SELECTED","${listNilai}")
                if (listNilai.size > 0) {

                    listNilai.map {nilai ->
                        nilai.tanggal
                    }.forEach {
                        listTanggal.add(it.toString())
                    }
                }
                Log.d("LIST_TANGGAL", "$listTanggal")

                tanggal_nilai1.setAdapter(null)
                tanggal_nilai1.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, listTanggal)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }



    private fun getLatestNilai(siswaId: String) {
        mFirestore.collection("nilai")
            .document(siswaId)
            .get()
            .addOnSuccessListener {
                val isi = it.toObject(Nilai::class.java)
                if (isi?.Penilaian_Sikap?.get("Sikap Spiritual") !=null) {
                    sikap_spiritual1.setText("${isi?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                }else if(isi?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                    sikap_sosial1.setText("${isi?.Penilaian_Sikap?.get("Sikap Sosial")}")
                }

                if (isi?.Kelas_Pra_Akademik?.get("Kelas Pra Akademik") != null) {
                    val akademik1 = isi?.Kelas_Pra_Akademik?.get("Kelas Pra Akademik")
                    for (i in akademik1!!.indices) {
                        if (mingguke_sikap_sosial == akademik1[i].minggu) {
                            materi_sikap_sosial1.setText(akademik1[i].materi)
                            ket_sikap_sosial1.setText(akademik1[i].keterangan)
                            nilai_sikap_sosial1.setText(akademik1[i].nilai.toString())
                            break
                        }
                    }
                }

                else if(isi?.Kelas_Komputer?.get("Materi")!=null){
                    nilaikomputer1.setText("${isi?.Kelas_Komputer?.get("Materi")}")
                }else if(isi?.Kelas_Komputer?.get("Keterangan")!=null){
                    ket_komputer1.setText("${isi?.Kelas_Komputer?.get("Keterangan")}")
                }else if(isi?.Kelas_Komputer?.get("Nilai")!=null){
                    nilai_komputer1.setText("${isi?.Kelas_Komputer?.get("Nilai")}")
                }else if (isi?.Kelas_Murajaah?.get("Materi")!=null){
                    materi_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Materi")}")
                }
                else if(isi?.Kelas_Murajaah?.get("Keterangan")!=null){
                    ket_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Keterangan")}")
                }
                else if(isi?.Kelas_Murajaah?.get("Nilai")!=null){
                    nilai_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Nilai")}")
                }
                else if (isi?.Kelas_Murajaah?.get("Kelas Murajaah") != null) {
                    val murajaah1 = isi?.Kelas_Murajaah?.get("Kelas Murajaah")
                    for (i in murajaah1!!.indices) {
                        if (mingguke_murajaah == murajaah1[i].minggu) {
                            materi_murajaah1.setText(murajaah1[i].materi)
                            ket_murajaah1.setText(murajaah1[i].keterangan)
                            nilai_murajaah1.setText(murajaah1[i].nilai.toString())
                            break
                        }
                    }

                }
                else if(isi?.Ekstrakulikuler?.get("Nama Ektra")!=null){
                    nama_ekstra1.setText("${isi?.Ekstrakulikuler?.get("Nama Ektra")}")
                }else if(isi?.Ekstrakulikuler?.get("Keterangan")!=null){
                    ket_ekstra1.setText("${isi?.Ekstrakulikuler?.get("Keterangan")}")
                }
                else if (isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")!=null){
                    val graph1= isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")
                    Log.d("TES ISI graph1", "$graph1")
                    val dataPoints =arrayOfNulls<DataPoint>(graph1!!.size)
                    for (i in graph1.indices) {
                        dataPoints[i] = DataPoint(
                            graph1[i].angka!!.plus(0.0),
                            graph1[i].minggu!!.plus(0.0)
                        )
                    }
                    val series = LineGraphSeries<DataPoint>(dataPoints)
                    val series2 = PointsGraphSeries<DataPoint>(dataPoints)
                    graph.addSeries(series)
                    graph.addSeries(series2)
                    series2.setShape(PointsGraphSeries.Shape.POINT)
                    series.setTitle("Perkembangan Anak")
                    graph.getLegendRenderer().setVisible(true)
                    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP)

                }
                else if (isi?.Saran_Guru?.get("Saran Guru")!=null){
                    saran_guru1.setText("${isi?.Saran_Guru?.get("Saran Guru")}")
                }else if(isi?.TbBb?.get("Tinggi Badan")!=null){
                    tinggi_badan1.setText("${isi?.TbBb?.get("Tinggi Badan")}")
                }else if (isi?.TbBb?.get("Berat Badan")!=null){
                    berat_badan1.setText("${isi?.TbBb?.get("Berat Badan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                    penglihatan1.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                    pendengaran1.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                }else if (isi?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                    daya_tahan1.setText("${isi?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                }else if(isi?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                    gigi1.setText("${isi?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                    kondisi_saat_ini1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                    kondisi_ideal1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                }else if(isi?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                    saran_dokter1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                }else if(isi?.Absensi?.get("Izin")!= null){
                    izin1.setText("${isi?.Absensi?.get("Izin")}")
                }else if(isi?.Absensi?.get("Sakit")!=null){
                    sakit1.setText("${isi?.Absensi?.get("Sakit")}")
                }else if (isi?.Absensi?.get("Tanpa Keterangan")!=null){
                    tidak_ada_keterangan1.setText("${isi?.Absensi?.get("Tanpa Keterangan")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                    kondisi_psikologi_saat_ini1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                    kondisi_ideal_psikologi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                    saran_psikolog1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                    kondisi_okupasi_saat_ini1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                    kondisi_ideal_okupasi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                }else if(isi?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                    saran_okupasi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }


    }

    fun getDataSiswa(siswaId: String) {
        mFirestore.collection("students").document(siswaId)
            .get()
            .addOnSuccessListener {
                val isi=it.toObject(Siswa::class.java)
                Glide.with(this)
                    .asBitmap()
                    .thumbnail(0.5f)
                    .load(isi?.cover)
                    .into(logo_siswa1)

                nama_siswa1.setText(isi?.nama)
                tempat_tanggal_lahir1.setText(isi?.ttl)
                jenis_kelamin1.setText(isi?.gender)
                alamat_siswa1.setText(isi?.alamat)
                tingkatan_kelas1.setText(isi?.kelas)
                nisn_siswa1.setText(isi?.nisn)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  {
        val id = item!!.itemId
        if (id==android.R.id.home){
            (activity as AppCompatActivity).onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}

