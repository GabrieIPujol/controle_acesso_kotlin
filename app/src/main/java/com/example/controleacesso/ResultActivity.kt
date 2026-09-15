package com.example.controleacesso

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Encontrar as views pelo ID
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        // Receber os dados enviados pela MainActivity
        val nome = intent.getStringExtra("NOME") ?: ""
        val autorizado = intent.getBooleanExtra("AUTORIZADO", false)

        tvResultado.text = if (autorizado) {
            "$nome, você está AUTORIZADO"
        } else {
            "$nome, você NÃO ESTÁ autorizado"
        }

        // Configurar o listener para o botão Voltar
        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
