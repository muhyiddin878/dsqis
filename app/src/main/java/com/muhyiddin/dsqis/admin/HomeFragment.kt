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

    lateinit var uname:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uname = arguments!!.getString("USERNAME")
        (activity as AdminActivity).setActionBarTitle(uname)
        return inflater.inflate(R.layout.activity_home_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val greetings = view.findViewById<TextView>(R.id.greetings)

        val username = view.findViewById<EditText>(R.id.username)
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        val edit = view.findViewById<Button>(R.id.edit_info_admin)
        val save = view.findViewById<Button>(R.id.simpan_perubahan)




        greetings.text = "Selamat datang, $uname"

        username.isFocusable = false
        username.isClickable = false
        username.isLongClickable = false
        password.isFocusable = false
        password.isClickable = false
        password.isLongClickable = false
        email.isFocusable = false
        email.isClickable = false
        email.isLongClickable = false

        save.isEnabled = false

        edit.setOnClickListener {
            username.isFocusableInTouchMode = true
            username.isLongClickable = true
            email.isFocusableInTouchMode = true
            email.isLongClickable = true
            password.isFocusableInTouchMode = true
            password.isLongClickable = true
            save.isEnabled = true
            edit.isEnabled = false
        }
        save.setOnClickListener {
//            setAdminInfo()
            username.isFocusable = false
            username.isClickable = false
            username.isLongClickable = false
            password.isFocusable = false
            password.isClickable = false
            password.isLongClickable = false
            email.isFocusable = false
            email.isClickable = false
            email.isLongClickable = false

            save.isEnabled = false
            edit.isEnabled = true
        }
    }


}
