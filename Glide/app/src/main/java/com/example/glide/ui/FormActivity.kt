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

/**
 * Activity que permite crear o editar un usuario.
 *
 * - Modo crear (POST) si no se recibe [USER_ID] por intent.
 * - Modo editar (PUT) si se recibe [USER_ID], cargando los datos existentes.
 * - Observa LiveData del [FormViewModel] para mostrar estado de carga, errores y resultados.
 */
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

        // ── Inicializar views ─────────────────────────────────────
        tvMode = findViewById(R.id.tvMode)
        btnGuardar = findViewById(R.id.btnGuardar)
        etNom = findViewById(R.id.etNom)
        etFeina = findViewById(R.id.etFeina)
        progressBar = findViewById(R.id.progressBar)
        tvResultat = findViewById(R.id.tvResultat)
        cardResultat = findViewById(R.id.cardResultat)

        // ── Obtener datos del Intent ─────────────────────────────
        userId = intent.getStringExtra("USER_ID")
        val nomExistent = intent.getStringExtra("USER_NOM") ?: ""
        val jobExistent = intent.getStringExtra("USER_JOB") ?: ""

        // ── Configurar modo CREAR o EDITAR según userId ─────────
        if (userId == null) {
            // ── Modo crear (POST) ───────────────────────────────
            supportActionBar?.title = "Nou usuari"
            tvMode.text = "🟦 MODE CREAR"
            tvMode.setBackgroundColor(0xFF1565C0.toInt())
            btnGuardar.text = "Crear usuari"
        } else {
            // ── Modo editar (PUT) ──────────────────────────────
            supportActionBar?.title = "Editar usuari #$userId"
            tvMode.text = "🟩 MODE EDITAR"
            tvMode.setBackgroundColor(0xFF2E7D32.toInt())
            btnGuardar.text = "Guardar canvis"
            etNom.setText(nomExistent)
            etFeina.setText(jobExistent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ── Configurar click del botón Guardar ────────────────
        btnGuardar.setOnClickListener {
            val nom   = etNom.text.toString().trim()
            val feina = etFeina.text.toString().trim()

            // ── Validación básica ─────────────────────────────
            if (nom.isEmpty())   { etNom.error = "Camp obligatori";   return@setOnClickListener }
            if (feina.isEmpty()) { etFeina.error = "Camp obligatori"; return@setOnClickListener }

            // ── Llamar al ViewModel según modo ───────────────
            val id = userId
            if (id == null) {
                viewModel.crear(nom, feina)
            } else {
                viewModel.actualitzar(id, nom, feina)
            }
        }

        // ── Observar estado de carga ─────────────────────────
        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            btnGuardar.isEnabled   = !loading
        }

        // ── Observar mensajes del ViewModel ─────────────────
        viewModel.missatge.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        // ── Observar resultado de la operación ──────────────
        viewModel.resultat.observe(this) { res ->
            res?.let {
                tvResultat.text       = it
                cardResultat.visibility = View.VISIBLE
            }
        }

        // ── Observar errores ─────────────────────────────────
        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    /**
     * Maneja la acción de back en el ActionBar.
     *
     * @return true siempre, finaliza la activity.
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}