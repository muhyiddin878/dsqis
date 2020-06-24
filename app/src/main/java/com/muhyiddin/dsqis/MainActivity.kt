package com.muhyiddin.dsqis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var prefs: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs=AppPreferences(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        frg2()



//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//            .addOnCompleteListener { task ->
//                var msg = "Subscribed"
//                if (!task.isSuccessful) {
//                    msg = "Failed to subscribe"
//                }
//                Log.d("ISI MSG", msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//            }





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
                if (prefs.isLoading==false){
                    item.setCheckable(true)
                    Toast.makeText(this, "MASUK HALAMAN PROFIL", Toast.LENGTH_SHORT).show()
                    frg3()
                }else{
                    item.setCheckable(false)
                    Toast.makeText(this, "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.timeline -> {
                if(prefs.isLoading==false){
                    item.setCheckable(true)
                    Toast.makeText(this, "MASUK BERANDA ARTIKEL", Toast.LENGTH_SHORT).show()
                    frg()
                }else{
                    item.setCheckable(false)
                    Toast.makeText(this, "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.chat -> {
                Toast.makeText(this, "MASUK CHATROOM", Toast.LENGTH_SHORT).show()
                frg2()

            }
            R.id.laporan -> {
                if(prefs.isLoading==false){
                    item.setCheckable(true)
                    Toast.makeText(this, "MASUK HALAMAN LAPORAN", Toast.LENGTH_SHORT).show()
                    frg4()
                }else{
                    item.setCheckable(false)
                    Toast.makeText(this, "Mohon Tunggu Sebentar", Toast.LENGTH_SHORT).show()
                }

            }
        }

        return true




    }

    fun runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        // [END fcm_runtime_enable_auto_init]
    }


//    private fun getToken() {
//
//        Thread(Runnable {
//            try {
//
//                val newToken = FirebaseInstanceId.getInstance()
//                    .getToken(senderID, "FCM")
//                println("Token --> $newToken")
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }).start()
//    }



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

