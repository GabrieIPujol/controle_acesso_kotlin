package com.example.controleacesso

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontrar as views pelo ID
        val etNome = findViewById<EditText>(R.id.etNome)
        val etContatoEmergencia = findViewById<EditText>(R.id.etContatoEmergencia)
        val etDataNascimento = findViewById<EditText>(R.id.etDataNascimento)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        // Configurar o seletor de data ao tocar no campo
        etDataNascimento.setOnClickListener {
            val calendarAtual = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { _, ano, mes, dia ->
                    // mes vem de 0 a 11, então soma 1 ao formatar
                    val dataFormatada = String.format(Locale("pt", "BR"), "%02d/%02d/%04d", dia, mes + 1, ano)
                    etDataNascimento.setText(dataFormatada)
                },
                calendarAtual.get(Calendar.YEAR),
                calendarAtual.get(Calendar.MONTH),
                calendarAtual.get(Calendar.DAY_OF_MONTH)
            )

            // Não deixa escolher uma data futura
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        // Configurar o listener para o botão
        btnEntrar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val contatoEmergencia = etContatoEmergencia.text.toString().trim()
            val dataNascimento = etDataNascimento.text.toString().trim()

            // Verificar se todos os campos foram preenchidos
            if (nome.isEmpty() || contatoEmergencia.isEmpty() || dataNascimento.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idade = calcularIdade(dataNascimento)

            if (idade == null) {
                Toast.makeText(this, "Data de nascimento inválida. Use dd/mm/aaaa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val autorizado = idade >= 18

            // Enviar os dados para a tela de resultado
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("NOME", nome)
            intent.putExtra("AUTORIZADO", autorizado)
            startActivity(intent)
        }
    }

    // Calcula a idade a partir de uma data no formato dd/MM/yyyy.
    // Retorna null se a data digitada for inválida.
    private fun calcularIdade(dataNascimentoTexto: String): Int? {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        formato.isLenient = false

        return try {
            val dataNascimento = formato.parse(dataNascimentoTexto) ?: return null

            val calendarNascimento = Calendar.getInstance()
            calendarNascimento.time = dataNascimento

            val calendarHoje = Calendar.getInstance()

            var idade = calendarHoje.get(Calendar.YEAR) - calendarNascimento.get(Calendar.YEAR)

            // Ajusta caso o aniversário deste ano ainda não tenha acontecido
            if (calendarHoje.get(Calendar.DAY_OF_YEAR) < calendarNascimento.get(Calendar.DAY_OF_YEAR)) {
                idade--
            }

            idade
        } catch (e: Exception) {
            null
        }
    }
}
