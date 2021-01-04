package com.igordesouza.whatsclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        this.auth = FirebaseAuth.getInstance()

        tbCadastro.setNavigationOnClickListener {
            abreMainActivity()
        }

        btnCadastro.setOnClickListener {
            cadastro()
        }

    }

    private fun cadastro() {
        if (validacaoCadastro()){

            cadastroCarregando(true)

            this.auth.createUserWithEmailAndPassword(edtEmailCadastro.text.toString(),
                                                edtSenhaCadastro.text.toString()
            ).addOnCompleteListener { cadastro ->

                if (cadastro.isSuccessful){

                    this.auth.currentUser!!.sendEmailVerification().addOnCompleteListener { envioEmail ->

                        if (envioEmail.isSuccessful){

                            sucessoCadastro()

                        } else {

                            this.auth.currentUser!!.delete().addOnCompleteListener {
                                toast(envioEmail.exception?.message!!)
                                cadastroCarregando(false)
                            }
                        }
                    }
                } else {

                    this.toast("Falha ao realizar a operação. Motivo: ${cadastro.exception?.message}")
                    cadastroCarregando(false)
                }

            }

        } else {

            this.toast("Verifique o(s) campo(s) incorreto(s)")

        }
    }

    private fun sucessoCadastro() {
        this.toast("Cadastro realizado com sucesso! E-mail de verificação enviado com sucesso!")
        abreMainActivity()
    }

    private fun cadastroCarregando(flag: Boolean) {
        edtNomeCadastro.isEnabled = !flag
        edtEmailCadastro.isEnabled = !flag
        edtSenhaCadastro.isEnabled = !flag
        btnCadastro.isEnabled = !flag

        btnCadastro.text = if(flag) "Carregando..." else "Cadastrar"

        pgCadastro.isVisible = flag
    }

    private fun toast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun validacaoCadastro(): Boolean {

        if (edtNomeCadastro.text.isEmpty() || edtNomeCadastro.text.isBlank()){
            edtNomeCadastro.error = "Nome de usuário inválido."
            edtNomeCadastro.requestFocus()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmailCadastro.text).matches() ||
                    edtEmailCadastro.text.isEmpty() || edtEmailCadastro.text.isBlank()){
            edtEmailCadastro.error = "Endereço de e-mail inválido."
            edtEmailCadastro.requestFocus()
            return false
        } else if (edtSenhaCadastro.text.isEmpty() || edtSenhaCadastro.text.isBlank()){
            edtSenhaCadastro.error = "Senha inválida."
            edtSenhaCadastro.requestFocus()
            return false
        } else if (edtSenhaCadastro.text.length < 6){
            edtSenhaCadastro.error = "Senha deve ter no mínimo 6 caracteres."
            edtSenhaCadastro.requestFocus()
        }

        return true
    }

    private fun abreMainActivity() {
        startActivity( Intent(this, MainActivity::class.java))
        this.finish()
    }


}