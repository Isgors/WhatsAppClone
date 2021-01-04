package com.igordesouza.whatsclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnCadastro

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        this.onStart()
    }

    override fun onStart() {
        super.onStart()
        val auth :FirebaseAuth = FirebaseAuth.getInstance()
        val user :FirebaseUser? = auth.currentUser

        if (user != null){
            if (!user.isEmailVerified){

                abreVerifiqueEmailActivity()

            } else {

                abreInicioActivity()

            }
        }

        btnCadastro.setOnClickListener{
            abreCadastroActivity()
        }
        
        btnEntrar.setOnClickListener { 

            if(validacaoLogin()){

                loginCarregando(true)

                auth.signInWithEmailAndPassword(
                    edtEmailLogin.text.toString(),
                    edtSenhaLogin.text.toString()
                ).addOnCompleteListener { login ->

                    if(login.isSuccessful){

                        abreInicioActivity()

                    } else {

                        Toast.makeText(this, "Autenticação falhou! Verifique seu e-mail e senha.", Toast.LENGTH_SHORT).show()

                    }

                }

            } else {

                Toast.makeText(this, "Verifique o(s) campo(s) incorreto(s)", Toast.LENGTH_SHORT).show()

            }
        
        }
        
    }

    private fun loginCarregando(flag: Boolean) {

        edtEmailLogin.isEnabled = !flag

        edtSenhaLogin.isEnabled = !flag

        btnCadastro.isEnabled = !flag

        btnEntrar.isEnabled = !flag

        btnEntrar.text = if(flag) "Carregando..." else "Entrar"

        pgLogin.isVisible = flag


    }

    private fun validacaoLogin(): Boolean {

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmailLogin.text).matches() ||
            edtEmailLogin.text.isEmpty() || edtEmailLogin.text.isBlank()){
            edtEmailLogin.error = "Endereço de e-mail inválido."
            edtEmailLogin.requestFocus()
            return false
        } else if (edtSenhaLogin.text.isEmpty() || edtSenhaLogin.text.isBlank()){
            edtSenhaLogin.error = "Senha inválida."
            edtSenhaLogin.requestFocus()
            return false
        } else if (edtSenhaLogin.text.length < 6){
            edtSenhaLogin.error = "Senha deve ter no mínimo 6 caracteres."
            edtSenhaLogin.requestFocus()
        }

        return true
    }

    private fun abreInicioActivity() {

        startActivity(Intent(this, InicioActivity::class.java))
        this.finish()
    }

    private fun abreVerifiqueEmailActivity() {
        startActivity(Intent(this, VerifiqueEmailActivity::class.java))
        this.finish()
    }

    private fun abreCadastroActivity() {
        startActivity(Intent(this, CadastroActivity::class.java))
        this.finish()
    }
}