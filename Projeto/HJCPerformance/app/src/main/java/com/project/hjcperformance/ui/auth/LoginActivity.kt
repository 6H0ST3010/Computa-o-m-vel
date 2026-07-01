package com.project.hjcperformance.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.project.hjcperformance.R
import com.project.hjcperformance.repository.UserRepository
import com.project.hjcperformance.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    // Injeção do UserRepository para gerir a criação do perfil na Firestore
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtRegistar = findViewById<TextView>(R.id.txtRegistar)

        // Ação do Botão Login
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // Login com sucesso -> Vai para a MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Fecha o Login para não voltar atrás com o botão Back
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro no acesso: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Ação para Criar Conta integrada com a Firestore (Passo 1)
        txtRegistar.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Insira email e passe nos campos para registar.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 1. Cria o utilizador no Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    // Extrai o início do email para usar como nome padrão temporário
                    val nomePadrao = email.substringBefore("@").replaceFirstChar { it.uppercase() }

                    // 2. Cria o documento correspondente na coleção "users" do Firestore através do Repository
                    userRepository.criarPerfilUtilizador(nomePadrao, email) { sucesso ->
                        if (sucesso) {
                            Toast.makeText(this, "Mecânico Registado na Firestore!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Conta criada, mas falhou ao inicializar perfil no Firestore.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao registar: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}