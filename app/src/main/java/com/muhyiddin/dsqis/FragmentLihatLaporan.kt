package com.muhyiddin.dsqis

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.adapter.ListSiswaAdapter
import com.muhyiddin.dsqis.model.Nilai
import com.muhyiddin.dsqis.model.Siswa
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_detail_siswa_fragment.*
import kotlinx.android.synthetic.main.activity_fragment_lihat_laporan.*


class FragmentLihatLaporan : Fragment() {
    private lateinit var mapel: String
    private val mFirestore = FirebaseFirestore.getInstance()
    private lateinit var idSiswa: String
    lateinit var prefs: AppPreferences
    private val CHOOSE_IMAGE = 101
    var resolver: ContentResolver?=null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.title = "Laporan Anak"
        return inflater.inflate(R.layout.activity_fragment_lihat_laporan, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mata_pelajaran = view.findViewById<Spinner>(R.id.pelajaran1)
        prefs=AppPreferences(context)
        getIdFromParent()

        val foto = view.findViewById<ImageView>(R.id.logo_siswa1)
        resolver = getActivity()?.getApplicationContext()!!.getContentResolver()


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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.VISIBLE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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
                    perkembangan_anak1.visibility = View.GONE

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


    private fun getIdFromParent() {
        Log.d("ini uid",prefs.uid)
        mFirestore.collection("parents")
            .document(prefs.uid).collection("students")
            .get()
            .addOnSuccessListener {
                Log.d("ini size it","${it.size()}")
                for (siswa in it) {
                    val datasiswa = siswa.toObject(Siswa::class.java)
                    Log.d("INI data siswa", "$datasiswa")
                    idSiswa = datasiswa.id
                    getLatestNilai(idSiswa)
                    getDataSiswa(idSiswa)
                }

            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getLatestNilai(siswaId: String) {
        mFirestore.collection("nilai")
            .document(siswaId)
            .get()
            .addOnSuccessListener {
                //                Log.d("ini isi tes ",it.data.toString() )
                val isi = it.toObject(Nilai::class.java)
                sikap_spiritual1.setText("${isi?.Penilaian_Sikap?.get("Sikap Spiritual")}")
                sikap_sosial1.setText("${isi?.Penilaian_Sikap?.get("Sikap Sosial")}")
//                mingguke1
                materi_sikap_sosial1.setText("${isi?.Kelas_Pra_Akademik?.get("Materi")}")
                ket_sikap_sosial1.setText("${isi?.Kelas_Pra_Akademik?.get("Keterangan")}")
                nilai_sikap_sosial1.setText("${isi?.Kelas_Pra_Akademik?.get("Nilai")}")

                nilaikomputer1.setText("${isi?.Kelas_Komputer?.get("Materi")}")
                ket_komputer1.setText("${isi?.Kelas_Komputer?.get("Keterangan")}")
                nilai_komputer1.setText("${isi?.Kelas_Komputer?.get("Nilai")}")

//                mingguke_murajaah
                materi_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Materi")}")
                ket_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Keterangan")}")
                nilai_murajaah1.setText("${isi?.Kelas_Murajaah?.get("Nilai")}")


                nama_ekstra1.setText("${isi?.Ekstrakulikuler?.get("Nama Ektra")}")
                ket_ekstra1.setText("${isi?.Ekstrakulikuler?.get("Keterangan")}")

                perkembangan_anak1.setText("${isi?.Laporan_Perkembangan_Anak?.get("Perkembangan Anak")}")

                saran_guru1.setText("${isi?.Saran_Guru?.get("Saran Guru")}")

                tinggi_badan1.setText("${isi?.TbBb?.get("Tinggi Badan")}")
                berat_badan1.setText("${isi?.TbBb?.get("Berat Badan")}")

                penglihatan1.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Penglihatan")}")
                pendengaran1.setText("${isi?.Kondisi_Kesehatan?.get("Kesehatan Pendengaran")}")
                daya_tahan1.setText("${isi?.Kondisi_Kesehatan?.get("Daya Tahan")}")
                gigi1.setText("${isi?.Kondisi_Kesehatan?.get("Kondisi Gigi")}")

                kondisi_saat_ini1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Saat Ini")}")
                kondisi_ideal1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Kondisi Ideal")}")
                saran_dokter1.setText("${isi?.Evaluasi_Pertumbuhan_Anak?.get("Saran Dokter")}")

                izin1.setText("${isi?.Absensi?.get("Izin")}")
                sakit1.setText("${isi?.Absensi?.get("Sakit")}")
                tidak_ada_keterangan1.setText("${isi?.Absensi?.get("Tanpa Keterangan")}")

                kondisi_psikologi_saat_ini1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Saat Ini")}")
                kondisi_ideal_psikologi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Psikologi Ideal")}")
                saran_psikolog1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Psikolog")}")

                kondisi_okupasi_saat_ini1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Saat Ini")}")
                kondisi_ideal_okupasi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Kondisi Okupasi Ideal")}")
                saran_okupasi1.setText("${isi?.Evaluasi_Perkembangan_Anak?.get("Saran Okupasi")}")
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

}

