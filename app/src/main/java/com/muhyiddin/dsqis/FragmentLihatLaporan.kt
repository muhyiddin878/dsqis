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
import kotlinx.android.synthetic.main.lihat_nilai.*
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
    private var Kelas_Komunal = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Sensori = HashMap<String, MutableList<Murajaah>>()
    private val listTanggal = mutableListOf<String>()
    private val listNilai = mutableListOf<Nilai>()

    private val komunal:MutableList<Murajaah> = mutableListOf()
    private val sensori:MutableList<Murajaah> = mutableListOf()
    private lateinit var isiMingguKomunal: String
    private lateinit var isiMingguSensori: String

    private val listMurajaah = mutableListOf<Murajaah>()
    private val listPraAkademik = mutableListOf<Murajaah>()
    private val listKomunal = mutableListOf<Murajaah>()
    private val listSensori = mutableListOf<Murajaah>()
    private val listPerkembangan = mutableListOf<Grafik>()
    private var nilai: Nilai? = null

    private val mingguKomunal = mutableListOf<String>()
    private val mingguSensori = mutableListOf<String>()
    private lateinit var spinnerKelas:Spinner
    private lateinit var kelas: String

    private var kelasSiswa=mutableListOf<String>()



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

        val spinnerKomunal= view.findViewById<Spinner>(R.id.mingguke_komunal1)
        val spinnerSensori=view.findViewById<Spinner>(R.id.mingguke_sensori1)
        spinnerKelas = view.findViewById(R.id.kelas1)

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

        spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                kelas = spinnerKelas.selectedItem.toString()
                getNilai(idSiswa!!,kelas)

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
                nilai_komunal1.setText("")
                ket_komunal1.setText("")
                materi_komunal1.setText("")
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
                nilai_sensori1.setText("")
                ket_sensori1.setText("")
                materi_sensori1.setText("")
                setSensoriValue(isiMingguSensori)

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

                            if (nilai?.Laporan_Perkembangan_Anak!=null){
                                val graph3= nilai?.Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")
                                Log.d("TES ISI graph2", "$graph3")
                                val dataPoints =arrayOfNulls<DataPoint>(graph3!!.size)
                                Log.d("datapoint", "${dataPoints}")
                                for (i in graph3.indices) {
                                    dataPoints[i] = DataPoint(
                                        graph3[i].angka!!.plus(0.0),
                                        graph3[i].minggu!!.plus(0.0)
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

                        "Semua" -> {
                            if (nilai?.Penilaian_Sikap?.get("Sikap Spiritual")!=null){
                                sikap_spiritual1.setText("${nilai?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                            }
                            if(nilai?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                                sikap_sosial1.setText("${nilai?.Penilaian_Sikap?.get("Sikap Sosial")}")
                            }

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

                            if(nilai?.Kelas_Pra_Akademik!=null){
                                Log.d("Pra Akademik", "${nilai?.Kelas_Pra_Akademik}")
                                Kelas_Pra_Akademik = nilai?.Kelas_Pra_Akademik!!
                                Kelas_Pra_Akademik?.get("Kelas Pra Akademik")?.forEach {
                                    listPraAkademik.add(it)
                                }
                                Log.d("LIST_MURAJAAH", "$listPraAkademik")
                            }

                            if(nilai?.Kelas_Komputer?.get("Materi")!=null){
                                nilaikomputer1.setText("${nilai?.Kelas_Komputer?.get("Materi")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Keterangan")!=null){
                                ket_komputer1.setText("${nilai?.Kelas_Komputer?.get("Keterangan")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Nilai")!=null){
                                nilai_komputer1.setText("${nilai?.Kelas_Komputer?.get("Nilai")}")
                            }

                            if(nilai?.Kelas_Murajaah!=null){
                                Log.d("MURAJAAH", "${nilai?.Kelas_Murajaah}")
                                Kelas_Murajaah = nilai?.Kelas_Murajaah!!
                                Kelas_Murajaah?.get("Kelas Murajaah")?.forEach {
                                    listMurajaah.add(it)
                                }
                                Log.d("LIST_MURAJAAH", "$listMurajaah")
                            }

                            if(nilai?.Ekstrakulikuler?.get("Nama Ekstra")!=null){
                                nama_ekstra1.setText("${nilai?.Ekstrakulikuler?.get("Nama Ekstra")}")
                            }
                            if(nilai?.Ekstrakulikuler?.get("Keterangan")!=null){
                                ket_ekstra1.setText("${nilai?.Ekstrakulikuler?.get("Keterangan")}")
                            }

                            if (nilai?.Laporan_Perkembangan_Anak!=null){
                                val graph3= nilai?.Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")
                                Log.d("TES ISI graph2", "$graph3")
                                val dataPoints =arrayOfNulls<DataPoint>(graph3!!.size)
                                Log.d("datapoint", "${dataPoints}")
                                for (i in graph3.indices) {
                                    dataPoints[i] = DataPoint(
                                        graph3[i].angka!!.plus(0.0),
                                        graph3[i].minggu!!.plus(0.0)
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

                            if(nilai?.Saran_Guru?.get("Saran Guru")!=null){
                                saran_guru1.setText("${nilai?.Saran_Guru?.get("Saran Guru")}")
                            }

                            if(nilai?.TbBb?.get("Tinggi Badan")!=null){
                                tinggi_badan1.setText("${nilai?.TbBb?.get("Tinggi Badan")}")
                            }
                            if(nilai?.TbBb?.get("Berat Badan")!=null){
                                berat_badan1.setText("${nilai?.TbBb?.get("Berat Badan")}")
                            }

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

                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                                kondisi_saat_ini1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                                kondisi_ideal1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                                saran_dokter1.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                            }

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
                    if (mapel == "-") {
                        mapel1()
                    }
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
                    if (mapel == "Semua") {
                        mapel14()
                    }
                    if(mapel=="Kelas Komunikasi Lanjutan"){
                        mapel1()

                    }
                    if(mapel=="Kelas Sensori Integrasi"){
                        mapel1()
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
                getDataSiswa(idSiswa)
                getKelasSiswa(idSiswa)


            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }




    }

    fun getNilai(idSiswa: String,kelas1:String){

        listTanggal.clear()
        listTanggal.add("Tanggal")
        var ref = mFirestore.collection("nilai")
        ref.whereEqualTo("idSiswa", idSiswa).whereEqualTo("kelasSiswa",kelas1).get()
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

//                tanggal_nilai1.setAdapter(null)
                if (tanggal_nilai1!=null){
                    tanggal_nilai1.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, listTanggal)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    fun getDataSiswa(siswaId: String) {
        mFirestore.collection("students").document(siswaId)
            .get()
            .addOnSuccessListener {
                val isi=it.toObject(Siswa::class.java)

                if (logo_siswa1!=null && nama_siswa1!=null && tempat_tanggal_lahir1!=null
                    && jenis_kelamin1!=null && alamat_siswa1!=null && tingkatan_kelas1!=null && nisn_siswa1!=null ){

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

    private fun setKomunalValue(minggukeKomunal:String){
        val komunal= listKomunal.find { it.minggu==minggukeKomunal}
        if(komunal!=null){
            val nilai=komunal.nilai
            materi_komunal1.setText(komunal.materi)
            ket_komunal1.setText(komunal.keterangan)
            nilai_komunal1.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setSensoriValue(minggukeSensori:String){
        val sensori= listSensori.find { it.minggu==minggukeSensori}
        if(sensori!=null){
            val nilai=sensori.nilai
            materi_sensori1.setText(sensori.materi)
            ket_sensori1.setText(sensori.keterangan)
            nilai_sensori1.setText(Integer.toString((nilai!!)))

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  {
        val id = item!!.itemId
        if (id==android.R.id.home){
            (activity as AppCompatActivity).onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    fun mapel1(){
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

        mingguke_komunal1.visibility=View.GONE
        tv_mingguke_komunal1.visibility = View.GONE
        mingguke_komunal1.visibility = View.GONE
        tv_materi_komunal1.visibility = View.GONE
        materi_komunal1.visibility = View.GONE
        tv_ket_komunal1.visibility = View.GONE
        ket_komunal1.visibility = View.GONE
        tv_nilai_komunal1.visibility = View.GONE
        nilai_komunal1.visibility = View.GONE

        mingguke_sensori1.visibility=View.GONE
        tv_mingguke_sensori1.visibility = View.GONE
        mingguke_sensori1.visibility = View.GONE
        tv_materi_sensori1.visibility = View.GONE
        materi_sensori1.visibility = View.GONE
        tv_ket_sensori1.visibility = View.GONE
        ket_sensori1.visibility = View.GONE
        tv_nilai_sensori1.visibility = View.GONE
        nilai_sensori1.visibility = View.GONE

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

//        tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.GONE
        tv_kondisi_okupasi_saat_ini1.visibility = View.GONE
        kondisi_okupasi_saat_ini1.visibility = View.GONE
        tv_kondisi_ideal_okupasi1.visibility = View.GONE
        kondisi_ideal_okupasi1.visibility = View.GONE
        tv_saran_okupasi1.visibility = View.GONE
        saran_okupasi1.visibility = View.GONE


        when(mapel){
            "Penilaian Sikap" ->{
                tv_spiritual1.visibility = View.VISIBLE
                sikap_spiritual1.visibility = View.VISIBLE
                tv_sosial1.visibility = View.VISIBLE
                sikap_sosial1.visibility = View.VISIBLE
            }
            "Kelas Pra Akademik" ->{
                tv_mingguke1.visibility = View.VISIBLE
                mingguke1.visibility = View.VISIBLE
                tv_materi1.visibility = View.VISIBLE
                materi_sikap_sosial1.visibility = View.VISIBLE
                tv_ket1.visibility = View.VISIBLE
                ket_sikap_sosial1.visibility = View.VISIBLE
                tv_nilai_sosial1.visibility = View.VISIBLE
                nilai_sikap_sosial1.visibility = View.VISIBLE
            }
            "Kelas Komputer"->{
                tv_materi_komputer1.visibility = View.VISIBLE
                nilaikomputer1.visibility = View.VISIBLE
                tv_ket_komputer1.visibility = View.VISIBLE
                ket_komputer1.visibility = View.VISIBLE
                tv_nilai_komputer1.visibility = View.VISIBLE
                nilai_komputer1.visibility = View.VISIBLE
            }
            "Kelas Murajaah" ->{
                tv_mingguke_murajaah1.visibility = View.VISIBLE
                minggukeMurajaah1.visibility = View.VISIBLE
                tv_materi_murajaah1.visibility = View.VISIBLE
                materi_murajaah1.visibility = View.VISIBLE
                tv_ket_murajaah1.visibility = View.VISIBLE
                ket_murajaah1.visibility = View.VISIBLE
                tv_nilai_murajaah1.visibility = View.VISIBLE
                nilai_murajaah1.visibility = View.VISIBLE
            }
            "Ekstrakulikuler" ->{
                tv_nama_ekstra1.visibility = View.VISIBLE
                nama_ekstra1.visibility = View.VISIBLE
                tv_ket_ekstra1.visibility = View.VISIBLE
                ket_ekstra1.visibility = View.VISIBLE
            }
            "Laporan Perkembangan Anak" ->{
                tv_laporan_perkembangan_anak1.visibility = View.VISIBLE
                graph.visibility=View.VISIBLE
            }
            "Saran Guru" ->{
                tv_saran_guru1.visibility = View.VISIBLE
                saran_guru1.visibility = View.VISIBLE
            }
            "Tinggi dan Berat Badan" ->{
                tv_tinggi_badan1.visibility = View.VISIBLE
                tinggi_badan1.visibility = View.VISIBLE
                tv_berat_badan1.visibility = View.VISIBLE
                berat_badan1.visibility = View.VISIBLE
            }
            "Kondisi Kesehatan" ->{
                tv_kondisi_kesehatan1.visibility = View.VISIBLE
                tv_penglihatan1.visibility = View.VISIBLE
                penglihatan1.visibility = View.VISIBLE
                tv_pendengaran1.visibility = View.VISIBLE
                pendengaran1.visibility = View.VISIBLE
                tv_gigi1.visibility = View.VISIBLE
                gigi1.visibility = View.VISIBLE
                tv_daya_tahan_tubuh1.visibility = View.VISIBLE
                daya_tahan1.visibility = View.VISIBLE
            }
            "Evaluasi Pertumbuhan Anak" ->{
                tv_evaluasi_pertumbuhan_anak1.visibility = View.VISIBLE
                tv_kondisi_anak_saat_ini1.visibility = View.VISIBLE
                kondisi_saat_ini1.visibility = View.VISIBLE
                tv_kondisi_ideal1.visibility = View.VISIBLE
                kondisi_ideal1.visibility = View.VISIBLE
                tv_saran_dokter1.visibility = View.VISIBLE
                saran_dokter1.visibility = View.VISIBLE
            }
            "Absensi" ->{
                tv_absensi1.visibility = View.VISIBLE
                tv_izin1.visibility = View.VISIBLE
                izin1.visibility = View.VISIBLE
                tv_sakit1.visibility = View.VISIBLE
                sakit1.visibility = View.VISIBLE
                tv_tidak_ada_ket1.visibility = View.VISIBLE
                tidak_ada_keterangan1.visibility = View.VISIBLE
            }
            "Evaluasi Perkembangan Anak" ->{
                tv_evaluasi_perkembangan_anak1.visibility = View.VISIBLE
                tv_kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
                kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
                tv_kondisi_ideal_psikologi1.visibility = View.VISIBLE
                kondisi_ideal_psikologi1.visibility = View.VISIBLE
                tv_saran_psikologi1.visibility = View.VISIBLE
                saran_psikolog1.visibility = View.VISIBLE

//                tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.VISIBLE
                tv_kondisi_okupasi_saat_ini1.visibility = View.VISIBLE
                kondisi_okupasi_saat_ini1.visibility = View.VISIBLE
                tv_kondisi_ideal_okupasi1.visibility = View.VISIBLE
                kondisi_ideal_okupasi1.visibility = View.VISIBLE
                tv_saran_okupasi1.visibility = View.VISIBLE
                saran_okupasi1.visibility = View.VISIBLE
            }
            "Kelas Komunikasi Lanjutan" ->{
                mingguke_komunal1.visibility=View.VISIBLE
                tv_mingguke_komunal1.visibility = View.VISIBLE
                mingguke_komunal1.visibility = View.VISIBLE
                tv_materi_komunal1.visibility = View.VISIBLE
                materi_komunal1.visibility = View.VISIBLE
                tv_ket_komunal1.visibility = View.VISIBLE
                ket_komunal1.visibility = View.VISIBLE
                tv_nilai_komunal1.visibility = View.VISIBLE
                nilai_komunal1.visibility = View.VISIBLE
            }
            "Kelas Sensori Integrasi" ->{
                mingguke_sensori1.visibility=View.VISIBLE
                tv_mingguke_sensori1.visibility = View.VISIBLE
                mingguke_sensori1.visibility = View.VISIBLE
                tv_materi_sensori1.visibility = View.VISIBLE
                materi_sensori1.visibility = View.VISIBLE
                tv_ket_sensori1.visibility = View.VISIBLE
                ket_sensori1.visibility = View.VISIBLE
                tv_nilai_sensori1.visibility = View.VISIBLE
                nilai_sensori1.visibility = View.VISIBLE
            }
        }
    }

    fun mapel14(){
        tv_spiritual1.visibility = View.VISIBLE
        sikap_spiritual1.visibility = View.VISIBLE
        tv_sosial1.visibility = View.VISIBLE
        sikap_sosial1.visibility = View.VISIBLE

        tv_mingguke1.visibility = View.VISIBLE
        mingguke1.visibility = View.VISIBLE
        tv_materi1.visibility = View.VISIBLE
        materi_sikap_sosial1.visibility = View.VISIBLE
        tv_ket1.visibility = View.VISIBLE
        ket_sikap_sosial1.visibility = View.VISIBLE
        tv_nilai_sosial1.visibility = View.VISIBLE
        nilai_sikap_sosial1.visibility = View.VISIBLE

        mingguke_komunal1.visibility=View.VISIBLE
        tv_mingguke_komunal1.visibility = View.VISIBLE
        mingguke_komunal1.visibility = View.VISIBLE
        tv_materi_komunal1.visibility = View.VISIBLE
        materi_komunal1.visibility = View.VISIBLE
        tv_ket_komunal1.visibility = View.VISIBLE
        ket_komunal1.visibility = View.VISIBLE
        tv_nilai_komunal1.visibility = View.VISIBLE
        nilai_komunal1.visibility = View.VISIBLE

        mingguke_sensori1.visibility=View.VISIBLE
        tv_mingguke_sensori1.visibility = View.VISIBLE
        mingguke_sensori1.visibility = View.VISIBLE
        tv_materi_sensori1.visibility = View.VISIBLE
        materi_sensori1.visibility = View.VISIBLE
        tv_ket_sensori1.visibility = View.VISIBLE
        ket_sensori1.visibility = View.VISIBLE
        tv_nilai_sensori1.visibility = View.VISIBLE
        nilai_sensori1.visibility = View.VISIBLE

        tv_materi_komputer1.visibility = View.VISIBLE
        nilaikomputer1.visibility = View.VISIBLE
        tv_ket_komputer1.visibility = View.VISIBLE
        ket_komputer1.visibility = View.VISIBLE
        tv_nilai_komputer1.visibility = View.VISIBLE
        nilai_komputer1.visibility = View.VISIBLE


        tv_mingguke_murajaah1.visibility = View.VISIBLE
        minggukeMurajaah1.visibility = View.VISIBLE
        tv_materi_murajaah1.visibility = View.VISIBLE
        materi_murajaah1.visibility = View.VISIBLE
        tv_ket_murajaah1.visibility = View.VISIBLE
        ket_murajaah1.visibility = View.VISIBLE
        tv_nilai_murajaah1.visibility = View.VISIBLE
        nilai_murajaah1.visibility = View.VISIBLE

        tv_nama_ekstra1.visibility = View.VISIBLE
        nama_ekstra1.visibility = View.VISIBLE
        tv_ket_ekstra1.visibility = View.VISIBLE
        ket_ekstra1.visibility = View.VISIBLE


        tv_laporan_perkembangan_anak1.visibility = View.VISIBLE
//                    perkembangan_anak1.visibility = View.VISIBLE
        graph.visibility=View.VISIBLE

        tv_saran_guru1.visibility = View.VISIBLE
        saran_guru1.visibility = View.VISIBLE

        tv_tinggi_badan1.visibility = View.VISIBLE
        tinggi_badan1.visibility = View.VISIBLE
        tv_berat_badan1.visibility = View.VISIBLE
        berat_badan1.visibility = View.VISIBLE

        tv_kondisi_kesehatan1.visibility = View.VISIBLE
        tv_penglihatan1.visibility = View.VISIBLE
        penglihatan1.visibility = View.VISIBLE
        tv_pendengaran1.visibility = View.VISIBLE
        pendengaran1.visibility = View.VISIBLE
        tv_gigi1.visibility = View.VISIBLE
        gigi1.visibility = View.VISIBLE
        tv_daya_tahan_tubuh1.visibility = View.VISIBLE
        daya_tahan1.visibility = View.VISIBLE

        tv_evaluasi_pertumbuhan_anak1.visibility = View.VISIBLE
        tv_kondisi_anak_saat_ini1.visibility = View.VISIBLE
        kondisi_saat_ini1.visibility = View.VISIBLE
        tv_kondisi_ideal1.visibility = View.VISIBLE
        kondisi_ideal1.visibility = View.VISIBLE
        tv_saran_dokter1.visibility = View.VISIBLE
        saran_dokter1.visibility = View.VISIBLE

        tv_absensi1.visibility = View.VISIBLE
        tv_izin1.visibility = View.VISIBLE
        izin1.visibility = View.VISIBLE
        tv_sakit1.visibility = View.VISIBLE
        sakit1.visibility = View.VISIBLE
        tv_tidak_ada_ket1.visibility = View.VISIBLE
        tidak_ada_keterangan1.visibility = View.VISIBLE

        tv_evaluasi_perkembangan_anak1.visibility = View.VISIBLE
        tv_kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
        kondisi_psikologi_saat_ini1.visibility = View.VISIBLE
        tv_kondisi_ideal_psikologi1.visibility = View.VISIBLE
        kondisi_ideal_psikologi1.visibility = View.VISIBLE
        tv_saran_psikologi1.visibility = View.VISIBLE
        saran_psikolog1.visibility = View.VISIBLE

//        tv_evaluasi_perkembangan_anak_okupasi1.visibility = View.VISIBLE
        tv_kondisi_okupasi_saat_ini1.visibility = View.VISIBLE
        kondisi_okupasi_saat_ini1.visibility = View.VISIBLE
        tv_kondisi_ideal_okupasi1.visibility = View.VISIBLE
        kondisi_ideal_okupasi1.visibility = View.VISIBLE
        tv_saran_okupasi1.visibility = View.VISIBLE
        saran_okupasi1.visibility = View.VISIBLE

    }



}

