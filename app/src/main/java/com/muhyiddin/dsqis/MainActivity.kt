package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = AppPreferences(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        frg2()




    }
    fun frg(){
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frame_container, FragmentArtikel()).commit()
    }

    fun frg2(){
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frame_container, FragmentChatPakar()).commit()
    }
    fun frg3(){
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frame_container, FragmentProfil()).commit()
    }
    fun frg4(){
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.frame_container, FragmentLihatLaporan()).commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profil -> {
                Toast.makeText(this, "MASUK HALAMAN PROFIL", Toast.LENGTH_SHORT).show()
//                supportActionBar?.title="Profil"
                frg3()
            }
            R.id.timeline -> {
                Toast.makeText(this, "MASUK BERANDA ARTIKEL", Toast.LENGTH_SHORT).show()
                frg()

            }
            R.id.chat -> {
                Toast.makeText(this, "MASUK CHATROOM", Toast.LENGTH_SHORT).show()
                frg2()

            }
            R.id.laporan -> {
                Toast.makeText(this, "MASUK HALAMAN LAPORAN", Toast.LENGTH_SHORT).show()
                frg4()

            }
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        val backStackCount = supportFragmentManager.backStackEntryCount
//        Log.d("ON BACK PRESS", "$backStackCount")
//        if (backStackCount > 0) {
//            supportFragmentManager.popBackStack()
//            Log.d("ON BACK PRESS", "$backStackCount")
//        }
    }
}

