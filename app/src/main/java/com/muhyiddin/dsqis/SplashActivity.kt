package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.muhyiddin.dsqis.admin.AdminActivity
import com.muhyiddin.dsqis.utils.AppPreferences
import com.muhyiddin.qis.login.login
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    lateinit var prefs:AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        prefs = AppPreferences(this)

        Handler().postDelayed({
            if (prefs.uid == null || prefs.role == null){
                startActivity(Intent(this, login::class.java))
                finish()
            }
            if (prefs.uid!=null && prefs.role==1 ) {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            if (prefs.uid!=null && prefs.role==2 ) {
                startActivity(Intent(this,MainActivityPakar::class.java))
                finish()
            }
            if(prefs.uid!=null && prefs.role==3 ){
                startActivity(Intent(this,AdminActivity::class.java).putExtra("USERNAME",prefs.nama))
                finish()
            }
        },2500)

    }
}
