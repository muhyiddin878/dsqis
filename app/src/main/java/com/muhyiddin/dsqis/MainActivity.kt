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






//        keluar.setOnClickListener() {
//            val builder = AlertDialog.Builder(this)
//            // Set the alert dialog title
//            builder.setTitle("Hapus")
//            builder.setMessage("Are you want to set the app background color to RED?")
//            // Set a positive button and its click listener on alert dialog
//            builder.setPositiveButton("YES"){dialog, which ->
//                FirebaseAuth.getInstance().signOut()
//                prefs.resetPreference()
//                startActivity(Intent(this,login::class.java))
//                finish()
//            }
//            // Display a negative button on alert dialog
//            builder.setNegativeButton("No"){dialog,which ->
//                Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
//            }
//
//
//            // Display a neutral button on alert dialog
//            builder.setNeutralButton("Cancel"){_,_ ->
//                Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
//            }
//            builder.show()
//
//        }


    }
    fun frg(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, FragmentArtikel()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profil -> {
                Log.d("TAG", "tes masuk")
                Toast.makeText(this, "TES PROFIL", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,NewPostActivity::class.java))
            }
            R.id.timeline -> {
                Log.d("TAG", "tes masuk")
                Toast.makeText(this, "MASUK BERANDA ARTIKEL", Toast.LENGTH_SHORT).show()
                frg()

            }
        }

        return true
    }
}

