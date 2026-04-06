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
 * Actividad para crear o editar un usuario.
 *
 * - Permite introducir nombre, apellido y URL de avatar.
 * - En modo creación (userId == null), realiza un POST a la API.
 * - En modo edición (userId != null), realiza un PUT a la API.
 * - Muestra el resultado de la operación en una tarjeta informativa.
 */
class FormActivity : AppCompatActivity() {

    private val viewModel: FormViewModel by viewModels()
    private var userId: String? = null

    private lateinit var tvMode: TextView
    private lateinit var btnGuardar: Button
    private lateinit var etNom: EditText
    private lateinit var etCognom: EditText
    private lateinit var etAvatar: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvResultat: TextView
    private lateinit var cardResultat: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // ── Inicialización de views ────────────────────────────────
        tvMode = findViewById(R.id.tvMode)
        btnGuardar = findViewById(R.id.btnGuardar)
        etNom = findViewById(R.id.etNom)
        etCognom = findViewById(R.id.etFeina) // Se reutiliza el ID del layout anterior
        etAvatar = findViewById(R.id.etAvatar) // Nuevo campo en el XML (necesitarás añadirlo)
        progressBar = findViewById(R.id.progressBar)
        tvResultat = findViewById(R.id.tvResultat)
        cardResultat = findViewById(R.id.cardResultat)

        // ── Obtener datos del Intent ──────────────────────────────
        userId = intent.getStringExtra("USER_ID")
        val nomExistent = intent.getStringExtra("USER_NOM") ?: ""
        val cognomExistent = intent.getStringExtra("USER_COGNOM") ?: ""
        val avatarExistent = intent.getStringExtra("USER_AVATAR") ?: ""

        // ── Configurar modo (Crear/Editar) ────────────────────────
        if (userId == null) {
            supportActionBar?.title = "Nou usuari"
            tvMode.text = "🟦 MODE CREAR"
            tvMode.setBackgroundColor(0xFF1565C0.toInt())
            btnGuardar.text = "Crear usuari"
        } else {
            supportActionBar?.title = "Editar usuari #$userId"
            tvMode.text = "🟩 MODE EDITAR"
            tvMode.setBackgroundColor(0xFF2E7D32.toInt())
            btnGuardar.text = "Guardar canvis"
            etNom.setText(nomExistent)
            etCognom.setText(cognomExistent)
            etAvatar.setText(avatarExistent)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ── Lógica del botón guardar ──────────────────────────────
        btnGuardar.setOnClickListener {
            val nom    = etNom.text.toString().trim()
            val cognom = etCognom.text.toString().trim()
            val avatar = etAvatar.text.toString().trim()

            if (nom.isEmpty())    { etNom.error = "Camp obligatori";    return@setOnClickListener }
            if (cognom.isEmpty()) { etCognom.error = "Camp obligatori"; return@setOnClickListener }

            val id = userId
            if (id == null) {
                viewModel.crear(nom, cognom, avatar)
            } else {
                viewModel.actualitzar(id, nom, cognom, avatar)
            }
        }

        // ── Observar LiveData del ViewModel ───────────────────────
        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            btnGuardar.isEnabled   = !loading
        }

        viewModel.missatge.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.resultat.observe(this) { res ->
            res?.let {
                tvResultat.text = it
                cardResultat.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
