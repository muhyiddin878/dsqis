package com.muhyiddin.dsqis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityPakar : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pakar)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        frg2()
    }
    fun frg(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentArtikel()).commit()
    }

    fun frg2(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentChatPakar()).commit()
    }
    fun frg3(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentProfil()).commit()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profil_pakar -> {
                Toast.makeText(this, "MASUK HALAMAN PROFIL", Toast.LENGTH_SHORT).show()
//                supportActionBar?.title="Profil"
                frg3()
            }
            R.id.timeline_pakar -> {
                Toast.makeText(this, "MASUK BERANDA ARTIKEL", Toast.LENGTH_SHORT).show()
                frg()

            }
            R.id.chat_pakar -> {
                Toast.makeText(this, "MASUK CHATROOM", Toast.LENGTH_SHORT).show()
                frg2()

            }

        }

        return true
    }

}

