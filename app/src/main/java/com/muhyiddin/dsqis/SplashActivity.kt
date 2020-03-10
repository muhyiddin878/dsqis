package com.muhyiddin.dsqis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
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
            } else {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        },2500)

//        val firestore = FirebaseFirestore.getInstance()
//
//        kirim.setOnClickListener {
//            progress_bar.visibility = View.VISIBLE
//            val text = nama_gambar.text.toString()
//
//            firestore.collection("gambar")
//                .add(hashMapOf("text" to text))
////                .document("1qw23er4")
////                .set()
//                .addOnSuccessListener {
//                    progress_bar.visibility = View.GONE
//                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    progress_bar.visibility = View.GONE
//                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
//                }
//        }
    }
}
