package com.example.glideretrofitdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.glideretrofitdemo.databinding.ActivityFormBinding
import com.example.glideretrofitdemo.viewmodel.FormViewModel

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding
    private val viewModel: FormViewModel by viewModels()
    private var userId = 0   // 0 = mode crear, >0 = mode editar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", 0)
        val nomExistent = intent.getStringExtra("USER_NOM") ?: ""
        val jobExistent = intent.getStringExtra("USER_JOB") ?: ""

        if (userId == 0) {
            // ── MODE CREAR (POST) ─────────────────────────────────────────
            supportActionBar?.title = "Nou usuari"
            binding.tvMode.text = "🟦 MODE CREAR → POST api/users"
            binding.tvMode.setBackgroundColor(0xFF1565C0.toInt())
            binding.btnGuardar.text = "Crear usuari"
        } else {
            // ── MODE EDITAR (PUT) ─────────────────────────────────────────
            supportActionBar?.title = "Editar usuari #$userId"
            binding.tvMode.text = "🟩 MODE EDITAR → PUT api/users/$userId"
            binding.tvMode.setBackgroundColor(0xFF2E7D32.toInt())
            binding.btnGuardar.text = "Guardar canvis"
            binding.etNom.setText(nomExistent)
            binding.etFeina.setText(jobExistent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnGuardar.setOnClickListener {
            val nom   = binding.etNom.text.toString().trim()
            val feina = binding.etFeina.text.toString().trim()

            if (nom.isEmpty())   { binding.etNom.error = "Camp obligatori";   return@setOnClickListener }
            if (feina.isEmpty()) { binding.etFeina.error = "Camp obligatori"; return@setOnClickListener }

            if (userId == 0) viewModel.crear(nom, feina)
            else             viewModel.actualitzar(userId, nom, feina)
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnGuardar.isEnabled   = !loading
        }

        viewModel.missatge.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.resultat.observe(this) { res ->
            res?.let {
                binding.tvResultat.text       = it
                binding.cardResultat.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
