package com.muhyiddin.dsqis.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.muhyiddin.dsqis.R
import kotlinx.android.synthetic.main.activity_home_fragment.*

class HomeFragment : Fragment() {

    lateinit var uname: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uname = arguments!!.getString("USERNAME")
//        (activity as AdminActivity).setActionBarTitle(uname)
        return inflater.inflate(R.layout.activity_home_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetings = view.findViewById<TextView>(R.id.greetings)

        greetings.text = "Selamat Datang, $uname"




    }
}
