package com.muhyiddin.dsqis.admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private var count: Int = 0
    private var uname:String=""
    lateinit var prefs: AppPreferences
    lateinit var toolbar: Toolbar
//    val prefs = AppPreferences(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        uname = intent.getStringExtra("USERNAME")

        //untuk memanggil default fragment yang diinginkan ketika aplikasi baru dijalankan
        val fragment = HomeFragment()
        prefs= AppPreferences(this)

        val bundle = Bundle()
        bundle.putString("USERNAME", uname)
        fragment.arguments = bundle

        val tx = supportFragmentManager.beginTransaction().addToBackStack(null)

        tx.replace(R.id.screen_area, fragment)
        tx.commit()


        toolbar = findViewById(R.id.toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView

        navigationView.itemIconTintList = null


        //untuk menset default item yang di select pada navigation drawer ketika activity baru dibuat
        navigationView.setCheckedItem(R.id.home)


        navigationView.setNavigationItemSelectedListener(this)
    }
    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (count == 0) {
                //                super.onBackPressed();
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Anda yakin mau logout ?")
                    .setCancelable(true)
                    .setPositiveButton("Ya",
                        DialogInterface.OnClickListener { dialog, which -> this@AdminActivity.finish() })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                        val navigationView = findViewById(R.id.nav_view) as NavigationView
//                        navigationView.setCheckedItem(R.id.dashboard)
                    })
                val alert = builder.create()
                alert.show()
            } else {
                var fragment2: Fragment? = null
                val fm = supportFragmentManager
                val ft = fm.beginTransaction().addToBackStack(null).setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    0,
                    0
                )

                val fragment = supportFragmentManager.findFragmentByTag("DETAIL_SISWA")

                        if (fragment != null && fragment.isVisible) {
                            fragment2 = ListAkunFragment()
                            ft.replace(R.id.screen_area, fragment2)
                            count = 1
                            ft.commit()
                            val navigationView = findViewById(R.id.nav_view) as NavigationView
                            navigationView.setCheckedItem(R.id.list_akun)
                        } else {
                            fragment2 = HomeFragment()
                            val `var` = Bundle()
                            `var`.putString("USERNAME", uname)
                            fragment2!!.setArguments(`var`)
                            ft.replace(R.id.screen_area, fragment2)
                            count = 0
                            ft.commit()
                            val navigationView = findViewById(R.id.nav_view) as NavigationView
                            navigationView.setCheckedItem(R.id.home)
                        }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null

        val id = item.itemId

        if (id == R.id.home) {
            fragment = HomeFragment()
            count = 0
        } else if (id == R.id.input_nilai) {
            fragment = InputNilaiFragment()
            count = 1
        } else if (id == R.id.buat_akun) {
            fragment = BuatAkunFragment()
            count = 1
        } else if (id == R.id.list_akun) {
            fragment = ListAkunFragment()
            count = 1
        } else if (id == R.id.input_bio_guru) {
            fragment = TambahPakarFragment()
            count = 1
        } else if (id == R.id.list_guru) {
            fragment = ListAkunPakarFragment()
            count = 1
        } else if(id==R.id.list_artikel_admin){
            fragment=ListArtikelFragment()
            count = 1
        }else if (id == R.id.logout) {
            val builder = AlertDialog.Builder(this)
            // Set the alert dialog title
            builder.setTitle("LOGOUT")
            builder.setMessage("Are you want to Logout?")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES") { dialog, which ->
                FirebaseAuth.getInstance().signOut()
                prefs.resetPreference()
                startActivity(Intent(this, login::class.java))
                finish()
            }
            // Display a negative button on alert dialog
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }


            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(this, "You cancelled the dialog.", Toast.LENGTH_SHORT).show()
            }
            builder.show()
        }

        if (fragment != null) {
            val `var` = Bundle()
            `var`.putString("USERNAME", uname)
            fragment.arguments = `var`
            val fm = supportFragmentManager
            val ft = fm.beginTransaction().addToBackStack(null)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

            //setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            //^^^ untuk memberi animasi ketika berpindah fragment
            ft.replace(R.id.screen_area, fragment)
            ft.commit()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun setActionBarTitle(title: String) {
        toolbar.setTitle(title)
    }
}
