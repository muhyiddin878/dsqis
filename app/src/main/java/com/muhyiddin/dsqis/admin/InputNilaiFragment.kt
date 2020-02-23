package com.muhyiddin.dsqis.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.muhyiddin.dsqis.R
import kotlinx.android.synthetic.main.activity_input_nilai_fragment.*

class InputNilaiFragment : Fragment() {

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

        val tugas = view.findViewById<EditText>(R.id.nilai_tugas)
        val uh = view.findViewById<EditText>(R.id.nilai_uh)
        val uts = view.findViewById<EditText>(R.id.nilai_uts)
        val uas = view.findViewById<EditText>(R.id.nilai_uas)

        val target:String = ""
        val smt:String = ""
        val mapel:String = ""

        tugas.setText("")
        uh.setText("")
        uts.setText("")
        uas.setText("")

        val submit_nilai = view.findViewById<Button>(R.id.submit_nilai)
    }
}
