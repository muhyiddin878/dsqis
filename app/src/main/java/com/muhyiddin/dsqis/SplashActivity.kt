package com.muhyiddin.dsqis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
