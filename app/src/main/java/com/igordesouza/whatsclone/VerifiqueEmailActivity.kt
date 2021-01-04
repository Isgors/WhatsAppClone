package com.igordesouza.whatsclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_verifique_email.*

class VerifiqueEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_verifique_email)

        val auth : FirebaseAuth = FirebaseAuth.getInstance()

        val user : FirebaseUser? = auth.currentUser

        tvEmail.text = auth.currentUser!!.email

        btnVerifiquei.setOnClickListener {
            carregando(true)

            btnVerifiquei.text = "Carregando..."

            user!!.reload().addOnCompleteListener {

                if (user.isEmailVerified){

                    abreMainActivy()

                } else {
                    toast("Parece que seu e-mail ainda não foi verificado.")

                    carregando(false)

                    btnVerifiquei.text = "Verifiquei"
                }

            }

        }

        btnReenviar.setOnClickListener {
            
            carregando(true)

            btnReenviar.text = "Carregando..."

            auth.currentUser!!.sendEmailVerification().addOnCompleteListener { envioEmail ->

                if (envioEmail.isSuccessful){

                    toast("E-mail de verificação re-enviado com sucesso!")

                } else {

                    toast(envioEmail.exception?.message.toString())

                }

                carregando(false)
                btnReenviar.text = "Não recebi o e-mail"


            }

        }

        btnSair.setOnClickListener {

            auth.signOut()

            abreMainActivy()

        }
    }

    private fun carregando(flag: Boolean) {

        btnReenviar.isEnabled = !flag
        btnSair.isEnabled = !flag
        btnVerifiquei.isEnabled = !flag

    }

    private fun abreMainActivy() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    private fun toast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}