package com.example.glide.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.glide.R
import com.example.glide.viewmodel.FormViewModel

class FormActivity : AppCompatActivity() {

    private val viewModel: FormViewModel by viewModels()
    private var userId: String? = null

    private lateinit var tvMode: TextView
    private lateinit var btnGuardar: Button
    private lateinit var etNom: EditText
    private lateinit var etFeina: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvResultat: TextView
    private lateinit var cardResultat: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        tvMode = findViewById(R.id.tvMode)
        btnGuardar = findViewById(R.id.btnGuardar)
        etNom = findViewById(R.id.etNom)
        etFeina = findViewById(R.id.etFeina)
        progressBar = findViewById(R.id.progressBar)
        tvResultat = findViewById(R.id.tvResultat)
        cardResultat = findViewById(R.id.cardResultat)

        userId = intent.getStringExtra("USER_ID")
        val nomExistent = intent.getStringExtra("USER_NOM") ?: ""
        val jobExistent = intent.getStringExtra("USER_JOB") ?: ""

        if (userId == null) {
            // ── MODE CREAR (POST) ─────────────────────────────────────────
            supportActionBar?.title = "Nou usuari"
            tvMode.text = "🟦 MODE CREAR"
            tvMode.setBackgroundColor(0xFF1565C0.toInt())
            btnGuardar.text = "Crear usuari"
        } else {
            // ── MODE EDITAR (PUT) ─────────────────────────────────────────
            supportActionBar?.title = "Editar usuari #$userId"
            tvMode.text = "🟩 MODE EDITAR"
            tvMode.setBackgroundColor(0xFF2E7D32.toInt())
            btnGuardar.text = "Guardar canvis"
            etNom.setText(nomExistent)
            etFeina.setText(jobExistent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnGuardar.setOnClickListener {
            val nom   = etNom.text.toString().trim()
            val feina = etFeina.text.toString().trim()

            if (nom.isEmpty())   { etNom.error = "Camp obligatori";   return@setOnClickListener }
            if (feina.isEmpty()) { etFeina.error = "Camp obligatori"; return@setOnClickListener }

            val id = userId
            if (id == null) {
                viewModel.crear(nom, feina)
            } else {
                viewModel.actualitzar(id, nom, feina)
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            btnGuardar.isEnabled   = !loading
        }

        viewModel.missatge.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.resultat.observe(this) { res ->
            res?.let {
                tvResultat.text       = it
                cardResultat.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
