package com.muhyiddin.qis.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhyiddin.dsqis.MainActivity
import com.muhyiddin.dsqis.MainActivityPakar
import com.muhyiddin.dsqis.OnBoardingActivity
import com.muhyiddin.dsqis.R
import com.muhyiddin.dsqis.admin.AdminActivity
import com.muhyiddin.dsqis.model.User
import com.muhyiddin.dsqis.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_login.*


class login : AppCompatActivity(){

    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
//    private val prefs = AppPreferences(this)
    private lateinit var prefs:AppPreferences
    val user = FirebaseAuth.getInstance().currentUser
    private val mStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        prefs = AppPreferences(this)

//        go_register.onClick {
//            startActivity<RegisterActivity>()
//            finish()
//        }

        go_login.setOnClickListener() {

            val email = email_pengguna.text.toString()
            val password = password_pengguna.text.toString()
            validate(email,password)
            doLogin(email, password)

        }
    }

    fun  validate(email:String,password:String):Boolean{
        if (email.isEmpty()) {
            email_pengguna.setError("Email wajib diisi !")
            email_pengguna.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_pengguna.setError("Masukkan format email yang benar !")
            email_pengguna.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            password_pengguna.setError("Password wajib diisi !")
            password_pengguna.requestFocus()
            return false
        }
        return true;
    }


    fun doLogin(email: String, password: String) {
        showLoading()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                prefs.uid = it.user?.uid
                mFirestore.collection("user")
                    .document("${prefs.uid}")
                    .get()
                    .addOnSuccessListener {snapshot ->
                        val user = snapshot.toObject(User::class.java)
                        if (user?.role!=null){
                        prefs.role = user.role
                        }
                        if (user?.nama!=null){
                            prefs.nama = user.nama
                        }
                            hideLoading()
                        if (prefs.role==1){
                            showLoginSuccess()
                        } else if (prefs.role == 2){
                            showLoginSuccessPakar()
                        }else if (prefs.role==3){
                            showLoginSuccessAdmin()
                        }
                    }
                    .addOnFailureListener {
                        prefs.resetPreference()
                        hideLoading()
                        showFailedLogin(it.localizedMessage)
                    }
            }.addOnFailureListener {
                prefs.resetPreference()
                hideLoading()
                showFailedLogin(it.localizedMessage)
            }
    }

    override fun onStart() {

        super.onStart()
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
//           info("AUTH = ${auth.currentUser?.displayName}, ${auth.currentUser?.uid}")
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLoginSuccess() {
        val ref = FirebaseStorage.getInstance().getReference("profilpic/${prefs.uid}")
        val cek= ref.downloadUrl
        Log.d("url:",cek.toString())
        if(cek==null){
            startActivity(Intent(this,OnBoardingActivity::class.java))
        }else{
            startActivity(Intent(this,MainActivity::class.java))
        }
        finish()
    }
    fun showLoginSuccessPakar(){
        val ref = FirebaseStorage.getInstance().getReference("profilpic/${prefs.uid}")
        val cek= ref.downloadUrl
        if(cek==null){
            startActivity(Intent(this,OnBoardingActivity::class.java))
        }else{
            startActivity(Intent(this,MainActivityPakar::class.java))
        }
        finish()
    }
    fun showLoginSuccessAdmin() {
        startActivity(Intent(this,AdminActivity::class.java).putExtra("USERNAME",prefs.nama))
        finish()
    }
    fun showLoginSuccess2(msg: String) {
        sendToast(msg)
    }
    fun showFailedLogin(msg: String) {
        sendToast(msg)
    }
}
