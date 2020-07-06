package com.muhyiddin.dsqis.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import com.muhyiddin.dsqis.ArtikelActivity
import com.muhyiddin.dsqis.FragmentLaporan
import com.muhyiddin.dsqis.MainActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.model.*
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_fragment_lihat_laporan.*
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.lihat_nilai.*
import java.util.HashMap


class DetailSiswaFragment : Fragment() {
    private lateinit var JK_SISWA: String
    private lateinit var  nama: EditText
    private lateinit var  ttl:EditText
    private lateinit var  alamat:EditText
    private lateinit var  nisn:EditText
    private lateinit var  simpan:Button
    private lateinit var  jk: RadioGroup
    private var  jk2: String=""
    private lateinit var  lk: RadioButton
    private lateinit var  pr:RadioButton
    private lateinit var persiapan_siswa:RadioButton
    private lateinit var pertama_siswa:RadioButton
    private lateinit var kedua_siswa:RadioButton
    private lateinit var bundlesiswa:Bundle

    private var siswa:Siswa? = null
    private val CHOOSE_IMAGE = 101
    var resolver: ContentResolver?=null
    private val mStorage = FirebaseStorage.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    lateinit var prefs:AppPreferences
    private var uriImageSiswa: Uri? = null
    lateinit var kls:RadioGroup
    lateinit var kelas_siswa:String
    private var kls2:String=""
    private lateinit var idSiswa:String
    private val listTanggal = mutableListOf<String>()
    private val listNilai = mutableListOf<Nilai>()
    private lateinit var mapel: String

    private lateinit var minggumurajaah: String
    private lateinit var mingguke_murajaah: String
    private lateinit var mingguke_murajaah_spinner:Spinner
    private lateinit var minggu: String
    private lateinit var mingguke_sikap_sosial: String
    private lateinit var mingguke_sikap_sosial_spinner:Spinner

    private var Kelas_Murajaah = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Pra_Akademik = HashMap<String, MutableList<Murajaah>>()
    private var Laporan_Perkembangan_Anak = HashMap<String, MutableList<Grafik>>()

    private val listMurajaah = mutableListOf<Murajaah>()
    private val listPraAkademik = mutableListOf<Murajaah>()
    private val listPerkembangan = mutableListOf<Grafik>()

    private lateinit var spinnerKelas:Spinner
    private lateinit var kelas: String

    private lateinit var isiMingguKomunal: String
    private lateinit var isiMingguSensori: String
    private val listKomunal = mutableListOf<Murajaah>()
    private val listSensori = mutableListOf<Murajaah>()
    private var Kelas_Komunal = HashMap<String, MutableList<Murajaah>>()
    private var Kelas_Sensori = HashMap<String, MutableList<Murajaah>>()
    private val mingguKomunal = mutableListOf<String>()
    private val mingguSensori = mutableListOf<String>()
    private val minggu_murajaah:MutableList<String> = mutableListOf()
    private val minggu_pra_akademik :MutableList<String> = mutableListOf()
    private var kelasSiswa=mutableListOf<String>()

    private var nilai: Nilai? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as AdminActivity).setActionBarTitle("Detail Akun Siswa")
        return inflater.inflate(R.layout.activity_detail_siswa_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foto = view.findViewById<ImageView>(R.id.logo_siswa)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val backStackCount = (activity as AdminActivity).supportFragmentManager.backStackEntryCount
        Log.d("Back Stack Count", "$backStackCount")
        prefs= AppPreferences(context)

        bundlesiswa = Bundle()
        val siswa = arguments?.getSerializable("siswa") as Siswa
        idSiswa=siswa.id
        getKelasSiswa(idSiswa)


        nama=view.findViewById(R.id.nama_siswa)
        ttl=view.findViewById(R.id.tempat_tanggal_lahir)
        alamat=view.findViewById(R.id.alamat_siswa)
        nisn=view.findViewById(R.id.nisn_siswa)
        jk = view.findViewById(R.id.jenis_kelamin_siswa) as RadioGroup
        kls = view.findViewById(R.id.tingkatan_kelas_siswa) as RadioGroup
        persiapan_siswa= view.findViewById(R.id.persiapan_siswa) as RadioButton
        pertama_siswa=view.findViewById(R.id.pertama_siswa) as RadioButton
        kedua_siswa=view.findViewById(R.id.kedua_siswa)
        lk = view.findViewById(R.id.lk_detail) as RadioButton
        pr = view.findViewById(R.id.pr_detail) as RadioButton
        simpan = view.findViewById(R.id.simpan_perubahan_siswa) as Button

        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran2)
        val spinnerKomunal= view.findViewById<Spinner>(R.id.mingguke_komunal2)
        val spinnerSensori=view.findViewById<Spinner>(R.id.mingguke_sensori2)
        spinnerKelas = view.findViewById(R.id.kelas2)


        nama.setText(siswa.nama)
        ttl.setText(siswa.ttl)
        alamat.setText(siswa.alamat)
        nisn.setText(siswa.nisn)
        JK_SISWA=siswa.gender
        kelas_siswa=siswa.kelas

        if (JK_SISWA == "Laki-laki") {
            lk.isChecked = true
        } else if (JK_SISWA == "Perempuan") {
            pr.isChecked = true
        }

        Log.d("Kelas",kelas_siswa)

        if (kelas_siswa == "Persiapan") {
            persiapan_siswa.isChecked = true
        } else if (kelas_siswa== "Tahun Pertama") {
            pertama_siswa.isChecked = true
        }else if(kelas_siswa=="Tahun Kedua"){
            kedua_siswa.isChecked=true
        }


        Glide.with(this)
            .asBitmap()
            .thumbnail(0.5f)
            .load(siswa.cover)
            .into(logo_siswa)

        foto.setOnClickListener() {
            showImageChooser()
        }
        jenis_kelamin_siswa.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.lk_detail -> jk2 = "Laki-laki"
                R.id.pr_detail -> jk2 = "Perempuan"
            }
        })
        tingkatan_kelas_siswa.setOnCheckedChangeListener (RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.persiapan_siswa -> kls2 = "Persiapan"
                R.id.pertama_siswa -> kls2 = "Tahun Pertama"
                R.id.kedua_siswa -> kls2 = "Tahun Kedua"
            }
        })
        simpan.setOnClickListener {
            updateSiswa(siswa!!.id,uriImageSiswa,siswa!!.nama,nama_siswa.text.toString(),tempat_tanggal_lahir.text.toString(),jk2,kls2,alamat_siswa.text.toString(),nisn_siswa.text.toString())

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
                nilai_komunal2.setText("")
                ket_komunal2.setText("")
                materi_komunal2.setText("")
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
                nilai_sensori2.setText("")
                ket_sensori2.setText("")
                materi_sensori2.setText("")
                setSensoriValue(isiMingguSensori)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }



        hapus_akun.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            // Set the alert dialog title
            builder.setTitle("HAPUS DATA")
            builder.setMessage("Apakah Anda Yakin Akan Menghapus Data?")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YA") { dialog, which ->
                hapusAkun(siswa.id,siswa.nama)
            }
            // Display a negative button on alert dialog
            builder.setNegativeButton("TIDAK") { dialog, which ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            // Display a neutral button on alert dialog
            builder.setNeutralButton("BATALKAN") { _, _ ->
                Toast.makeText(context, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }


        mingguke_murajaah_spinner = view.findViewById(R.id.minggukeMurajaah2)
        mingguke_sikap_sosial_spinner = view.findViewById(R.id.mingguke2)

        mingguke_sikap_sosial_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mingguke_sikap_sosial = mingguke_sikap_sosial_spinner.selectedItem.toString()
                nilai_sikap_sosial2.setText("")
                ket_sikap_sosial2.setText("")
                materi_sikap_sosial2.setText("")
                setPraAkademikValue( mingguke_sikap_sosial)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        mingguke_murajaah_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mingguke_murajaah =  mingguke_murajaah_spinner.selectedItem.toString()
                nilai_murajaah2.setText("")
                ket_murajaah2.setText("")
                materi_murajaah2.setText("")
                setMurajaahValue(mingguke_murajaah)

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        mata_pelajaran.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mapel = mata_pelajaran.selectedItem.toString()
                if (mapel == "-") {
                    tv_spiritual2.visibility = View.GONE
                    sikap_spiritual2.visibility = View.GONE
                    tv_sosial2.visibility = View.GONE
                    sikap_sosial2.visibility = View.GONE

                    tv_mingguke2.visibility = View.GONE
                    mingguke2.visibility = View.GONE
                    tv_materi2.visibility = View.GONE
                    materi_sikap_sosial2.visibility = View.GONE
                    tv_ket2.visibility = View.GONE
                    ket_sikap_sosial2.visibility = View.GONE
                    tv_nilai_sosial2.visibility = View.GONE
                    nilai_sikap_sosial2.visibility = View.GONE

                    tv_materi_komputer2.visibility = View.GONE
                    nilaikomputer2.visibility = View.GONE
                    tv_ket_komputer2.visibility = View.GONE
                    ket_komputer2.visibility = View.GONE
                    tv_nilai_komputer2.visibility = View.GONE
                    nilai_komputer2.visibility = View.GONE


                    tv_mingguke_murajaah2.visibility = View.GONE
                    minggukeMurajaah2.visibility = View.GONE
                    tv_materi_murajaah2.visibility = View.GONE
                    materi_murajaah2.visibility = View.GONE
                    tv_ket_murajaah2.visibility = View.GONE
                    ket_murajaah2.visibility = View.GONE
                    tv_nilai_murajaah2.visibility = View.GONE
                    nilai_murajaah2.visibility = View.GONE

                    tv_nama_ekstra2.visibility = View.GONE
                    nama_ekstra2.visibility = View.GONE
                    tv_ket_ekstra2.visibility = View.GONE
                    ket_ekstra2.visibility = View.GONE


                    tv_laporan_perkembangan_anak2.visibility = View.GONE
//                    perkembangan_anak2.visibility = View.GONE
                    graph2.visibility=View.GONE

                    tv_saran_guru2.visibility = View.GONE
                    saran_guru2.visibility = View.GONE
                    tv_tinggi_badan2.visibility = View.GONE
                    tinggi_badan2.visibility = View.GONE

                    tv_berat_badan2.visibility = View.GONE
                    berat_badan2.visibility = View.GONE

                    tv_kondisi_kesehatan2.visibility = View.GONE
                    tv_penglihatan2.visibility = View.GONE
                    penglihatan2.visibility = View.GONE
                    tv_pendengaran2.visibility = View.GONE
                    pendengaran2.visibility = View.GONE
                    tv_gigi2.visibility = View.GONE
                    gigi2.visibility = View.GONE

                    tv_daya_tahan_tubuh2.visibility = View.GONE
                    daya_tahan2.visibility = View.GONE

                    tv_evaluasi_pertumbuhan_anak2.visibility = View.GONE
                    tv_kondisi_anak_saat_ini2.visibility = View.GONE
                    kondisi_saat_ini2.visibility = View.GONE
                    tv_kondisi_ideal2.visibility = View.GONE
                    kondisi_ideal2.visibility = View.GONE
                    tv_saran_dokter2.visibility = View.GONE
                    saran_dokter2.visibility = View.GONE

                    tv_absensi2.visibility = View.GONE
                    tv_izin2.visibility = View.GONE
                    izin2.visibility = View.GONE
                    tv_sakit2.visibility = View.GONE
                    sakit2.visibility = View.GONE
                    tv_tidak_ada_ket2.visibility = View.GONE
                    tidak_ada_keterangan2.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak2.visibility = View.GONE
                    tv_kondisi_psikologi_saat_ini2.visibility = View.GONE
                    kondisi_psikologi_saat_ini2.visibility = View.GONE
                    tv_kondisi_ideal_psikologi2.visibility = View.GONE
                    kondisi_ideal_psikologi2.visibility = View.GONE
                    tv_saran_psikologi2.visibility = View.GONE
                    saran_psikolog2.visibility = View.GONE

                    tv_evaluasi_perkembangan_anak_okupasi2.visibility = View.GONE
                    tv_kondisi_okupasi_saat_ini2.visibility = View.GONE
                    kondisi_okupasi_saat_ini2.visibility = View.GONE
                    tv_kondisi_ideal_okupasi2.visibility = View.GONE
                    kondisi_ideal_okupasi2.visibility = View.GONE
                    tv_saran_okupasi2.visibility = View.GONE
                    saran_okupasi2.visibility = View.GONE

                }


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

        }


        tanggal_nilai2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (tanggal_nilai2.selectedItemPosition==0) {
                    minggu_pra_akademik.clear()
                    minggu_murajaah.clear()
                    mingguKomunal.clear()
                    mingguSensori.clear()

                    sikap_spiritual2.setText("")
                    sikap_sosial2.setText("")

                    nilaikomputer2.setText("")
                    ket_komputer2.setText("")
                    nilai_komputer2.setText("")

                    nama_ekstra2.setText("")
                    ket_ekstra2.setText("")

                    saran_guru2.setText("")

                    tinggi_badan2.setText("")
                    berat_badan2.setText("")

                    penglihatan2.setText("")
                    pendengaran2.setText("")
                    daya_tahan2.setText("")
                    gigi2.setText("")

                    kondisi_saat_ini2.setText("")
                    kondisi_ideal2.setText("")
                    saran_dokter2.setText("")

                    izin2.setText("")
                    sakit2.setText("")
                    tidak_ada_keterangan2.setText("")

                    kondisi_psikologi_saat_ini2.setText("")
                    kondisi_ideal_psikologi2.setText("")
                    saran_psikolog2.setText("")
                    kondisi_okupasi_saat_ini2.setText("")
                    tv_kondisi_ideal_okupasi2.setText("")
                    saran_okupasi2.setText("")



                } else {
                    nilai = listNilai.find {
                        it.tanggal == tanggal_nilai2.selectedItem
                    }

                    when(mata_pelajaran.selectedItem) {
                        "Penilaian Sikap" -> {
                            if (nilai?.Penilaian_Sikap?.get("Sikap Spiritual")!=null){
                                sikap_spiritual2.setText("${nilai?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                            }
                            if(nilai?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                                sikap_sosial2.setText("${nilai?.Penilaian_Sikap?.get("Sikap Sosial")}")
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
                                    minggu_pra_akademik.add(it.minggu!!)
                                }
                                Log.d("LIST_MURAJAAH", "$listPraAkademik")
                                val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, minggu_pra_akademik)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                mingguke_sikap_sosial_spinner.setAdapter(adapter)
                            }

                        }
                        "Kelas Komputer" -> {
                            if(nilai?.Kelas_Komputer?.get("Materi")!=null){
                                nilaikomputer2.setText("${nilai?.Kelas_Komputer?.get("Materi")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Keterangan")!=null){
                                ket_komputer2.setText("${nilai?.Kelas_Komputer?.get("Keterangan")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Nilai")!=null){
                                nilai_komputer2.setText("${nilai?.Kelas_Komputer?.get("Nilai")}")
                            }

                        }
                        "Kelas Muraja'ah" -> {
                            if(nilai?.Kelas_Murajaah!=null){
                                Log.d("MURAJAAH", "${nilai?.Kelas_Murajaah}")
                                Kelas_Murajaah = nilai?.Kelas_Murajaah!!
                                Kelas_Murajaah?.get("Kelas Murajaah")?.forEach {
                                    listMurajaah.add(it)
                                    minggu_murajaah.add(it.minggu!!)
                                }
                                Log.d("LIST_MURAJAAH", "$listMurajaah")
                                val adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, minggu_murajaah)
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                mingguke_murajaah_spinner.setAdapter(adapter)
                            }

                        }
                        "Ekstrakulikuler" -> {
                            if(nilai?.Ekstrakulikuler?.get("Nama Ekstra")!=null){
                                nama_ekstra2.setText("${nilai?.Ekstrakulikuler?.get("Nama Ekstra")}")
                            }
                            if(nilai?.Ekstrakulikuler?.get("Keterangan")!=null){
                                ket_ekstra2.setText("${nilai?.Ekstrakulikuler?.get("Keterangan")}")
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
                                graph2.addSeries(series)
                                graph2.addSeries(series2)
                                series2.setShape(PointsGraphSeries.Shape.POINT)
                                series.setTitle("Perkembangan Anak")
                                graph2.getLegendRenderer().setVisible(true)
                                graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP)

                            }

                        }
                        "Saran Guru" -> {
                            if(nilai?.Saran_Guru?.get("Saran Guru")!=null){
                                saran_guru2.setText("${nilai?.Saran_Guru?.get("Saran Guru")}")
                            }

                        }
                        "Tinggi dan Berat Badan" -> {
                            if(nilai?.TbBb?.get("Tinggi Badan")!=null){
                                tinggi_badan2.setText("${nilai?.TbBb?.get("Tinggi Badan")}")
                            }
                            if(nilai?.TbBb?.get("Berat Badan")!=null){
                                berat_badan2.setText("${nilai?.TbBb?.get("Berat Badan")}")
                            }

                        }
                        "Kondisi Kesehatan" -> {
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                                penglihatan2.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                                pendengaran2.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                                daya_tahan2.setText("${nilai?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                                gigi2.setText("${nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                            }

                        }
                        "Evaluasi Pertumbuhan Anak" -> {
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                                kondisi_saat_ini2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                                kondisi_ideal2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                                saran_dokter2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                            }

                        }
                        "Absensi" -> {

                            if(nilai?.Absensi?.get("Izin")!=null){
                                izin2.setText("${nilai?.Absensi?.get("Izin")}")
                            }
                            if(nilai?.Absensi?.get("Sakit")!=null){
                                sakit2.setText("${nilai?.Absensi?.get("Sakit")}")
                            }
                            if(nilai?.Absensi?.get("Tanpa Keterangan")!=null){
                                tidak_ada_keterangan2.setText("${nilai?.Absensi?.get("Tanpa Keterangan")}")
                            }

                        }
                        "Evaluasi Perkembangan Anak" -> {
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                                kondisi_psikologi_saat_ini2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                                kondisi_ideal_psikologi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                                saran_psikolog2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                                kondisi_okupasi_saat_ini2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                                kondisi_ideal_okupasi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                                saran_okupasi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                            }
                        }
                        "Semua"->{
                            if (nilai?.Penilaian_Sikap?.get("Sikap Spiritual")!=null){
                                sikap_spiritual2.setText("${nilai?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                            }
                            if(nilai?.Penilaian_Sikap?.get("Sikap Sosial")!=null){
                                sikap_sosial2.setText("${nilai?.Penilaian_Sikap?.get("Sikap Sosial")}")
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
                            }

                            if(nilai?.Kelas_Komputer?.get("Materi")!=null){
                                nilaikomputer2.setText("${nilai?.Kelas_Komputer?.get("Materi")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Keterangan")!=null){
                                ket_komputer2.setText("${nilai?.Kelas_Komputer?.get("Keterangan")}")
                            }
                            if(nilai?.Kelas_Komputer?.get("Nilai")!=null){
                                nilai_komputer2.setText("${nilai?.Kelas_Komputer?.get("Nilai")}")
                            }

                            if(nilai?.Kelas_Murajaah!=null){
                                Log.d("MURAJAAH", "${nilai?.Kelas_Murajaah}")
                                Kelas_Murajaah = nilai?.Kelas_Murajaah!!
                                Kelas_Murajaah?.get("Kelas Murajaah")?.forEach {
                                    listMurajaah.add(it)
                                }
                            }

                            if(nilai?.Ekstrakulikuler?.get("Nama Ekstra")!=null){
                                nama_ekstra2.setText("${nilai?.Ekstrakulikuler?.get("Nama Ekstra")}")
                            }
                            if(nilai?.Ekstrakulikuler?.get("Keterangan")!=null){
                                ket_ekstra2.setText("${nilai?.Ekstrakulikuler?.get("Keterangan")}")
                            }


                            if (nilai?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")!=null){
                                val graph3= nilai?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")
                                Log.d("TES ISI graph3", "$graph3")
                                val dataPoints =arrayOfNulls<DataPoint>(graph3!!.size)
                                for (i in graph3.indices) {
                                    dataPoints[i] = DataPoint(
                                        graph3[i].angka!!.plus(0.0),
                                        graph3[i].minggu!!.plus(0.0)
                                    )
                                }
                                val series = LineGraphSeries<DataPoint>(dataPoints)
                                val series2 = PointsGraphSeries<DataPoint>(dataPoints)
                                graph2.addSeries(series)
                                graph2.addSeries(series2)
                                series2.setShape(PointsGraphSeries.Shape.POINT)
                                series.setTitle("Perkembangan Anak")
                                graph2.getLegendRenderer().setVisible(true)
                                graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP)

                            }
                            if(nilai?.Laporan_Perkembangan_Anak!=null){
                                Log.d("Perkembangan", "${nilai?.Laporan_Perkembangan_Anak}")
                                Laporan_Perkembangan_Anak = nilai?.Laporan_Perkembangan_Anak!!
                                Laporan_Perkembangan_Anak?.get("Laporan Perkembangan Anak")?.forEach {
                                    listPerkembangan.add(it)
                                }
                            }

                            if(nilai?.Saran_Guru?.get("Saran Guru")!=null){
                                saran_guru2.setText("${nilai?.Saran_Guru?.get("Saran Guru")}")
                            }

                            if(nilai?.TbBb?.get("Tinggi Badan")!=null){
                                tinggi_badan2.setText("${nilai?.TbBb?.get("Tinggi Badan")}")
                            }
                            if(nilai?.TbBb?.get("Berat Badan")!=null){
                                berat_badan2.setText("${nilai?.TbBb?.get("Berat Badan")}")
                            }

                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")!=null){
                                penglihatan2.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")!=null){
                                pendengaran2.setText("${nilai?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Daya Tahan")!=null){
                                daya_tahan2.setText("${nilai?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                            }
                            if(nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")!=null){
                                gigi2.setText("${nilai?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")
                            }

                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")!=null){
                                kondisi_saat_ini2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")!=null){
                                kondisi_ideal2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")!=null){
                                saran_dokter2.setText("${nilai?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")
                            }

                            if(nilai?.Absensi?.get("Izin")!=null){
                                izin2.setText("${nilai?.Absensi?.get("Izin")}")
                            }
                            if(nilai?.Absensi?.get("Sakit")!=null){
                                sakit2.setText("${nilai?.Absensi?.get("Sakit")}")
                            }
                            if(nilai?.Absensi?.get("Tanpa Keterangan")!=null){
                                tidak_ada_keterangan2.setText("${nilai?.Absensi?.get("Tanpa Keterangan")}")
                            }


                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")!=null){
                                kondisi_psikologi_saat_ini2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")!=null){
                                kondisi_ideal_psikologi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")!=null){
                                saran_psikolog2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")!=null){
                                kondisi_okupasi_saat_ini2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")!=null){
                                kondisi_ideal_okupasi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                            }
                            if(nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")!=null){
                                saran_okupasi2.setText("${nilai?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
                            }

                        }



                    }
                }


            }

        }




    }

    private fun showImageChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto Anak"), CHOOSE_IMAGE)
    }

    fun updateSiswa(id:String, uri:Uri?, namaLama: String, namaBaru:String,ttlBaru:String,genderBaru:String,kelasBaru:String,alamatBaru: String,nisnBaru:String){
        var imageLocation = ""
//        showLoading()
        val dbRef = mFirestore.collection("students")
            .document(id)
        val ref = mStorage.getReference("students/$namaLama-$id")
        if (uri!=null){
            ref.putFile(uri).continueWithTask {
//                setProgressBarLength(45)
                if (!it.isSuccessful){
                    it.exception?.let {
                        throw it
                    }
                    hideLoading(false, "Error ${it.exception?.message}")
                }
                return@continueWithTask ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    imageLocation = it.result.toString()
                    dbRef.update(mapOf(
                        "cover" to imageLocation,
                        "nama" to namaBaru,
                        "ttl" to ttlBaru,
                        "gender" to genderBaru,
                        "kelas" to kelasBaru,
                        "alamat" to alamatBaru,
                        "nisn" to nisnBaru
                    )).addOnSuccessListener {
                        hideLoading(true, "BERHASIL DI UPDATE")
                    }
                        .addOnFailureListener {
                            hideLoading(false, "Error ${it.localizedMessage}")
                        }
                } else{
//                    info(it.exception?.message)
                    hideLoading(false, "Error ${it.exception?.message}")
                }
            }
        } else{
//            info("URI NULL")
            dbRef.update(mapOf(
                "nama" to namaBaru,
                "ttl" to ttlBaru,
                "gender" to genderBaru,
                "kelas" to kelasBaru,
                "alamat" to alamatBaru,
                "nisn" to nisnBaru
            )).addOnCompleteListener {
                hideLoading(true, "BERHASIL DI UPDATE")
            }
                .addOnFailureListener {
                    hideLoading(false, "Error ${it.localizedMessage}")
                }
        }

    }

    fun hideLoading(success:Boolean, message:String) {
        if (!success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else{
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunFragment())?.commit()
        }
    }

    private fun validate() {
        if (nama_siswa.text!!.isEmpty()){
            nama_siswa.setError("Nama belum diisi..")
            nama_siswa.requestFocus()
            return
        }
        if (JK_SISWA.isEmpty()){
            jenis_kelamin_siswa.requestFocus()
            return
        }
        if (tempat_tanggal_lahir.text!!.isEmpty()){
            tempat_tanggal_lahir.setError("TTL  belum diisi..")
            tempat_tanggal_lahir.requestFocus()
            return
        }

        if (nisn_siswa.text!!.isEmpty()){
            nisn_siswa.setError("Nama belum diisi..")
            nisn_siswa.requestFocus()
            return
        }
//        if (tingkatan_kelas.text!!.isEmpty()){
//            tingkatan_kelas.setError("Nama belum diisi..")
//            tingkatan_kelas.requestFocus()
//            return
//        }
        if (alamat_siswa.text!!.isEmpty()){
            alamat_siswa.setError("Alamat belum diisi..")
            alamat_siswa.requestFocus()
            return
        }
//        if (nomor.text!!.isEmpty()){
//            nomor.setError("Nama belum diisi..")
//            nomor.requestFocus()
//            return
//        }
//        if (namaortu.text!!.isEmpty()){
//            namaortu.setError("Nama belum diisi..")
//            namaortu.requestFocus()
//            return
//        }
//        if (email.text!!.isEmpty()){
//            email.setError("Email belum diisi..")
//            email.requestFocus()
//            return
//        }
//        if (password.text!!.isEmpty()){
//            password.setError("Password belum diisi..")
//            password.requestFocus()
//            return
//        }

        nama_siswa.isEnabled = false
        tempat_tanggal_lahir.isEnabled = false
        nisn_siswa.isEnabled=false
        alamat_siswa.isEnabled=false

        updateSiswa(siswa!!.id,uriImageSiswa,siswa!!.nama,nama_siswa.text.toString(),tempat_tanggal_lahir.text.toString(),jk2,kls2,alamat_siswa.text.toString(),nisn_siswa.text.toString())

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

                tanggal_nilai2.setAdapter(null)
                tanggal_nilai2.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_row, listTanggal)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
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

    private fun setMurajaahValue(minggukeMurajaah: String) {
        val murajaah = listMurajaah.find {it.minggu == minggukeMurajaah}
        if (murajaah!=null) {
            val nilaii=murajaah.nilai
            Log.d("nilai",Integer.toString(nilaii!!))
            Log.d("ket",murajaah.keterangan)
            Log.d("materi",murajaah.materi)
            ket_murajaah2.setText(murajaah.keterangan)
            materi_murajaah2.setText(murajaah.materi)
            nilai_murajaah2.setText(Integer.toString(nilaii!!))
        }
    }

    private fun setPraAkademikValue(minggukePraAkademik:String){
        val praAkademik = listPraAkademik.find { it.minggu==minggukePraAkademik }
        if(praAkademik!=null){
            val nilai=praAkademik.nilai
            materi_sikap_sosial2.setText(praAkademik.materi)
            ket_sikap_sosial2.setText(praAkademik.keterangan)
            nilai_sikap_sosial2.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setKomunalValue(minggukeKomunal:String){
        val komunal= listKomunal.find { it.minggu==minggukeKomunal}
        if(komunal!=null){
            val nilai=komunal.nilai
            materi_komunal2.setText(komunal.materi)
            ket_komunal2.setText(komunal.keterangan)
            nilai_komunal2.setText(Integer.toString((nilai!!)))

        }
    }

    private fun setSensoriValue(minggukeSensori:String){
        val sensori= listSensori.find { it.minggu==minggukeSensori}
        if(sensori!=null){
            val nilai=sensori.nilai
            materi_sensori2.setText(sensori.materi)
            ket_sensori2.setText(sensori.keterangan)
            nilai_sensori2.setText(Integer.toString((nilai!!)))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data!=null && data.data!=null){
            uriImageSiswa = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, uriImageSiswa)
            logo_siswa.setImageBitmap(bitmap)
            logo_siswa.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }



    private fun hapusAkun(id:String,namaSiswa:String){
        mFirestore.collection("students").document(id)
            .delete()
            .addOnSuccessListener {
                Log.d("SUKSES 1","SUKSES")
                mStorage.getReference("students/$namaSiswa").delete()
                    .addOnSuccessListener {
                        Log.d("SUKSES 2","SUKSES")
                        mFirestore.collection("parents")
                            .get()
                            .addOnSuccessListener {
                                it.forEach {
                                    it.reference.collection("students")
                                        .document(id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Log.d("SUKSES 3","SUKSES")
                                            mFirestore.collection("nilai")
                                                .whereEqualTo("idSiswa",id)
                                                .get()
                                                .addOnSuccessListener {
                                                        it.forEach { isi->
                                                            isi.reference.delete()
                                                        }
                                                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.screen_area, ListAkunFragment())?.commit()
                                                    Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show()
                                                }.addOnFailureListener {
                                                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                                                }
                                        }.addOnFailureListener {
                                            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    }.addOnFailureListener {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    fun mapel1(){
        tv_spiritual2.visibility = View.GONE
        sikap_spiritual2.visibility = View.GONE
        tv_sosial2.visibility = View.GONE
        sikap_sosial2.visibility = View.GONE

        tv_mingguke2.visibility = View.GONE
        mingguke2.visibility = View.GONE
        tv_materi2.visibility = View.GONE
        materi_sikap_sosial2.visibility = View.GONE
        tv_ket2.visibility = View.GONE
        ket_sikap_sosial2.visibility = View.GONE
        tv_nilai_sosial2.visibility = View.GONE
        nilai_sikap_sosial2.visibility = View.GONE

        mingguke_komunal2.visibility=View.GONE
        tv_mingguke_komunal2.visibility = View.GONE
        mingguke_komunal2.visibility = View.GONE
        tv_materi_komunal2.visibility = View.GONE
        materi_komunal2.visibility = View.GONE
        tv_ket_komunal2.visibility = View.GONE
        ket_komunal2.visibility = View.GONE
        tv_nilai_komunal2.visibility = View.GONE
        nilai_komunal2.visibility = View.GONE

        mingguke_sensori2.visibility=View.GONE
        tv_mingguke_sensori2.visibility = View.GONE
        mingguke_sensori2.visibility = View.GONE
        tv_materi_sensori2.visibility = View.GONE
        materi_sensori2.visibility = View.GONE
        tv_ket_sensori2.visibility = View.GONE
        ket_sensori2.visibility = View.GONE
        tv_nilai_sensori2.visibility = View.GONE
        nilai_sensori2.visibility = View.GONE

        tv_materi_komputer2.visibility = View.GONE
        nilaikomputer2.visibility = View.GONE
        tv_ket_komputer2.visibility = View.GONE
        ket_komputer2.visibility = View.GONE
        tv_nilai_komputer2.visibility = View.GONE
        nilai_komputer2.visibility = View.GONE


        tv_mingguke_murajaah2.visibility = View.GONE
        minggukeMurajaah2.visibility = View.GONE
        tv_materi_murajaah2.visibility = View.GONE
        materi_murajaah2.visibility = View.GONE
        tv_ket_murajaah2.visibility = View.GONE
        ket_murajaah2.visibility = View.GONE
        tv_nilai_murajaah2.visibility = View.GONE
        nilai_murajaah2.visibility = View.GONE

        tv_nama_ekstra2.visibility = View.GONE
        nama_ekstra2.visibility = View.GONE
        tv_ket_ekstra2.visibility = View.GONE
        ket_ekstra2.visibility = View.GONE


        tv_laporan_perkembangan_anak2.visibility = View.GONE
//                    perkembangan_anak2.visibility = View.GONE
        graph2.visibility=View.GONE

        tv_saran_guru2.visibility = View.GONE
        saran_guru2.visibility = View.GONE

        tv_tinggi_badan2.visibility = View.GONE
        tinggi_badan2.visibility = View.GONE
        tv_berat_badan2.visibility = View.GONE
        berat_badan2.visibility = View.GONE

        tv_kondisi_kesehatan2.visibility = View.GONE
        tv_penglihatan2.visibility = View.GONE
        penglihatan2.visibility = View.GONE
        tv_pendengaran2.visibility = View.GONE
        pendengaran2.visibility = View.GONE
        tv_gigi2.visibility = View.GONE
        gigi2.visibility = View.GONE
        tv_daya_tahan_tubuh2.visibility = View.GONE
        daya_tahan2.visibility = View.GONE

        tv_evaluasi_pertumbuhan_anak2.visibility = View.GONE
        tv_kondisi_anak_saat_ini2.visibility = View.GONE
        kondisi_saat_ini2.visibility = View.GONE
        tv_kondisi_ideal2.visibility = View.GONE
        kondisi_ideal2.visibility = View.GONE
        tv_saran_dokter2.visibility = View.GONE
        saran_dokter2.visibility = View.GONE

        tv_absensi2.visibility = View.GONE
        tv_izin2.visibility = View.GONE
        izin2.visibility = View.GONE
        tv_sakit2.visibility = View.GONE
        sakit2.visibility = View.GONE
        tv_tidak_ada_ket2.visibility = View.GONE
        tidak_ada_keterangan2.visibility = View.GONE

        tv_evaluasi_perkembangan_anak2.visibility = View.GONE
        tv_kondisi_psikologi_saat_ini2.visibility = View.GONE
        kondisi_psikologi_saat_ini2.visibility = View.GONE
        tv_kondisi_ideal_psikologi2.visibility = View.GONE
        kondisi_ideal_psikologi2.visibility = View.GONE
        tv_saran_psikologi2.visibility = View.GONE
        saran_psikolog2.visibility = View.GONE

        tv_evaluasi_perkembangan_anak_okupasi2.visibility = View.GONE
        tv_kondisi_okupasi_saat_ini2.visibility = View.GONE
        kondisi_okupasi_saat_ini2.visibility = View.GONE
        tv_kondisi_ideal_okupasi2.visibility = View.GONE
        kondisi_ideal_okupasi2.visibility = View.GONE
        tv_saran_okupasi2.visibility = View.GONE
        saran_okupasi2.visibility = View.GONE

        when(mapel){
            "Penilaian Sikap" ->{
                tv_spiritual2.visibility = View.VISIBLE
                sikap_spiritual2.visibility = View.VISIBLE
                tv_sosial2.visibility = View.VISIBLE
                sikap_sosial2.visibility = View.VISIBLE
            }
            "Kelas Pra Akademik" ->{
                tv_mingguke2.visibility = View.VISIBLE
                mingguke2.visibility = View.VISIBLE
                tv_materi2.visibility = View.VISIBLE
                materi_sikap_sosial2.visibility = View.VISIBLE
                tv_ket2.visibility = View.VISIBLE
                ket_sikap_sosial2.visibility = View.VISIBLE
                tv_nilai_sosial2.visibility = View.VISIBLE
                nilai_sikap_sosial2.visibility = View.VISIBLE
            }
            "Kelas Komputer"->{
                tv_materi_komputer2.visibility = View.VISIBLE
                nilaikomputer2.visibility = View.VISIBLE
                tv_ket_komputer2.visibility = View.VISIBLE
                ket_komputer2.visibility = View.VISIBLE
                tv_nilai_komputer2.visibility = View.VISIBLE
                nilai_komputer2.visibility = View.VISIBLE
            }
            "Kelas Muraja'ah" ->{
                tv_mingguke_murajaah2.visibility = View.VISIBLE
                minggukeMurajaah2.visibility = View.VISIBLE
                tv_materi_murajaah2.visibility = View.VISIBLE
                materi_murajaah2.visibility = View.VISIBLE
                tv_ket_murajaah2.visibility = View.VISIBLE
                ket_murajaah2.visibility = View.VISIBLE
                tv_nilai_murajaah2.visibility = View.VISIBLE
                nilai_murajaah2.visibility = View.VISIBLE
            }
            "Ekstrakulikuler" ->{
                tv_nama_ekstra2.visibility = View.VISIBLE
                nama_ekstra2.visibility = View.VISIBLE
                tv_ket_ekstra2.visibility = View.VISIBLE
                ket_ekstra2.visibility = View.VISIBLE
            }
            "Laporan Perkembangan Anak" ->{
                tv_laporan_perkembangan_anak2.visibility = View.VISIBLE
                graph.visibility=View.VISIBLE
            }
            "Saran Guru" ->{
                tv_saran_guru2.visibility = View.VISIBLE
                saran_guru2.visibility = View.VISIBLE
            }
            "Tinggi dan Berat Badan" ->{
                tv_tinggi_badan2.visibility = View.VISIBLE
                tinggi_badan2.visibility = View.VISIBLE
                tv_berat_badan2.visibility = View.VISIBLE
                berat_badan2.visibility = View.VISIBLE
            }
            "Kondisi Kesehatan" ->{
                tv_kondisi_kesehatan2.visibility = View.VISIBLE
                tv_penglihatan2.visibility = View.VISIBLE
                penglihatan2.visibility = View.VISIBLE
                tv_pendengaran2.visibility = View.VISIBLE
                pendengaran2.visibility = View.VISIBLE
                tv_gigi2.visibility = View.VISIBLE
                gigi2.visibility = View.VISIBLE
                tv_daya_tahan_tubuh2.visibility = View.VISIBLE
                daya_tahan2.visibility = View.VISIBLE
            }
            "Evaluasi Pertumbuhan Anak" ->{
                tv_evaluasi_pertumbuhan_anak2.visibility = View.VISIBLE
                tv_kondisi_anak_saat_ini2.visibility = View.VISIBLE
                kondisi_saat_ini2.visibility = View.VISIBLE
                tv_kondisi_ideal2.visibility = View.VISIBLE
                kondisi_ideal2.visibility = View.VISIBLE
                tv_saran_dokter2.visibility = View.VISIBLE
                saran_dokter2.visibility = View.VISIBLE
            }
            "Absensi" ->{
                tv_absensi2.visibility = View.VISIBLE
                tv_izin2.visibility = View.VISIBLE
                izin2.visibility = View.VISIBLE
                tv_sakit2.visibility = View.VISIBLE
                sakit2.visibility = View.VISIBLE
                tv_tidak_ada_ket2.visibility = View.VISIBLE
                tidak_ada_keterangan2.visibility = View.VISIBLE
            }
            "Evaluasi Perkembangan Anak" ->{
                tv_evaluasi_perkembangan_anak2.visibility = View.VISIBLE
                tv_kondisi_psikologi_saat_ini2.visibility = View.VISIBLE
                kondisi_psikologi_saat_ini2.visibility = View.VISIBLE
                tv_kondisi_ideal_psikologi2.visibility = View.VISIBLE
                kondisi_ideal_psikologi2.visibility = View.VISIBLE
                tv_saran_psikologi2.visibility = View.VISIBLE
                saran_psikolog2.visibility = View.VISIBLE

//                tv_evaluasi_perkembangan_anak_okupasi2.visibility = View.VISIBLE
                tv_kondisi_okupasi_saat_ini2.visibility = View.VISIBLE
                kondisi_okupasi_saat_ini2.visibility = View.VISIBLE
                tv_kondisi_ideal_okupasi2.visibility = View.VISIBLE
                kondisi_ideal_okupasi2.visibility = View.VISIBLE
                tv_saran_okupasi2.visibility = View.VISIBLE
                saran_okupasi2.visibility = View.VISIBLE
            }
            "Kelas Komunikasi Lanjutan" ->{
                mingguke_komunal2.visibility=View.VISIBLE
                tv_mingguke_komunal2.visibility = View.VISIBLE
                mingguke_komunal2.visibility = View.VISIBLE
                tv_materi_komunal2.visibility = View.VISIBLE
                materi_komunal2.visibility = View.VISIBLE
                tv_ket_komunal2.visibility = View.VISIBLE
                ket_komunal2.visibility = View.VISIBLE
                tv_nilai_komunal2.visibility = View.VISIBLE
                nilai_komunal2.visibility = View.VISIBLE
            }
            "Kelas Sensori Integrasi" ->{
                mingguke_sensori2.visibility=View.VISIBLE
                tv_mingguke_sensori2.visibility = View.VISIBLE
                mingguke_sensori2.visibility = View.VISIBLE
                tv_materi_sensori2.visibility = View.VISIBLE
                materi_sensori2.visibility = View.VISIBLE
                tv_ket_sensori2.visibility = View.VISIBLE
                ket_sensori2.visibility = View.VISIBLE
                tv_nilai_sensori2.visibility = View.VISIBLE
                nilai_sensori2.visibility = View.VISIBLE
            }
        }

    }
    fun mapel14(){
        tv_spiritual2.visibility = View.VISIBLE
        sikap_spiritual2.visibility = View.VISIBLE
        tv_sosial2.visibility = View.VISIBLE
        sikap_sosial2.visibility = View.VISIBLE

        tv_mingguke2.visibility = View.VISIBLE
        mingguke2.visibility = View.VISIBLE
        tv_materi2.visibility = View.VISIBLE
        materi_sikap_sosial2.visibility = View.VISIBLE
        tv_ket2.visibility = View.VISIBLE
        ket_sikap_sosial2.visibility = View.VISIBLE
        tv_nilai_sosial2.visibility = View.VISIBLE
        nilai_sikap_sosial2.visibility = View.VISIBLE

        mingguke_komunal2.visibility=View.VISIBLE
        tv_mingguke_komunal2.visibility = View.VISIBLE
        mingguke_komunal2.visibility = View.VISIBLE
        tv_materi_komunal2.visibility = View.VISIBLE
        materi_komunal2.visibility = View.VISIBLE
        tv_ket_komunal2.visibility = View.VISIBLE
        ket_komunal2.visibility = View.VISIBLE
        tv_nilai_komunal2.visibility = View.VISIBLE
        nilai_komunal2.visibility = View.VISIBLE

        mingguke_sensori2.visibility=View.VISIBLE
        tv_mingguke_sensori2.visibility = View.VISIBLE
        mingguke_sensori2.visibility = View.VISIBLE
        tv_materi_sensori2.visibility = View.VISIBLE
        materi_sensori2.visibility = View.VISIBLE
        tv_ket_sensori2.visibility = View.VISIBLE
        ket_sensori2.visibility = View.VISIBLE
        tv_nilai_sensori2.visibility = View.VISIBLE
        nilai_sensori2.visibility = View.VISIBLE

        tv_materi_komputer2.visibility = View.VISIBLE
        nilaikomputer2.visibility = View.VISIBLE
        tv_ket_komputer2.visibility = View.VISIBLE
        ket_komputer2.visibility = View.VISIBLE
        tv_nilai_komputer2.visibility = View.VISIBLE
        nilai_komputer2.visibility = View.VISIBLE


        tv_mingguke_murajaah2.visibility = View.VISIBLE
        minggukeMurajaah2.visibility = View.VISIBLE
        tv_materi_murajaah2.visibility = View.VISIBLE
        materi_murajaah2.visibility = View.VISIBLE
        tv_ket_murajaah2.visibility = View.VISIBLE
        ket_murajaah2.visibility = View.VISIBLE
        tv_nilai_murajaah2.visibility = View.VISIBLE
        nilai_murajaah2.visibility = View.VISIBLE

        tv_nama_ekstra2.visibility = View.VISIBLE
        nama_ekstra2.visibility = View.VISIBLE
        tv_ket_ekstra2.visibility = View.VISIBLE
        ket_ekstra2.visibility = View.VISIBLE


        tv_laporan_perkembangan_anak2.visibility = View.VISIBLE
//                    perkembangan_anak2.visibility = View.VISIBLE
        graph2.visibility=View.VISIBLE

        tv_saran_guru2.visibility = View.VISIBLE
        saran_guru2.visibility = View.VISIBLE

        tv_tinggi_badan2.visibility = View.VISIBLE
        tinggi_badan2.visibility = View.VISIBLE
        tv_berat_badan2.visibility = View.VISIBLE
        berat_badan2.visibility = View.VISIBLE

        tv_kondisi_kesehatan2.visibility = View.VISIBLE
        tv_penglihatan2.visibility = View.VISIBLE
        penglihatan2.visibility = View.VISIBLE
        tv_pendengaran2.visibility = View.VISIBLE
        pendengaran2.visibility = View.VISIBLE
        tv_gigi2.visibility = View.VISIBLE
        gigi2.visibility = View.VISIBLE
        tv_daya_tahan_tubuh2.visibility = View.VISIBLE
        daya_tahan2.visibility = View.VISIBLE

        tv_evaluasi_pertumbuhan_anak2.visibility = View.VISIBLE
        tv_kondisi_anak_saat_ini2.visibility = View.VISIBLE
        kondisi_saat_ini2.visibility = View.VISIBLE
        tv_kondisi_ideal2.visibility = View.VISIBLE
        kondisi_ideal2.visibility = View.VISIBLE
        tv_saran_dokter2.visibility = View.VISIBLE
        saran_dokter2.visibility = View.VISIBLE

        tv_absensi2.visibility = View.VISIBLE
        tv_izin2.visibility = View.VISIBLE
        izin2.visibility = View.VISIBLE
        tv_sakit2.visibility = View.VISIBLE
        sakit2.visibility = View.VISIBLE
        tv_tidak_ada_ket2.visibility = View.VISIBLE
        tidak_ada_keterangan2.visibility = View.VISIBLE

        tv_evaluasi_perkembangan_anak2.visibility = View.VISIBLE
        tv_kondisi_psikologi_saat_ini2.visibility = View.VISIBLE
        kondisi_psikologi_saat_ini2.visibility = View.VISIBLE
        tv_kondisi_ideal_psikologi2.visibility = View.VISIBLE
        kondisi_ideal_psikologi2.visibility = View.VISIBLE
        tv_saran_psikologi2.visibility = View.VISIBLE
        saran_psikolog2.visibility = View.VISIBLE

        tv_evaluasi_perkembangan_anak_okupasi2.visibility = View.VISIBLE
        tv_kondisi_okupasi_saat_ini2.visibility = View.VISIBLE
        kondisi_okupasi_saat_ini2.visibility = View.VISIBLE
        tv_kondisi_ideal_okupasi2.visibility = View.VISIBLE
        kondisi_ideal_okupasi2.visibility = View.VISIBLE
        tv_saran_okupasi2.visibility = View.VISIBLE
        saran_okupasi2.visibility = View.VISIBLE

    }




}
