package com.example.glide.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.glide.R
import com.example.glide.viewmodel.DetallViewModel

/**
 * Activity que muestra el detalle completo de un usuario ([User]).
 *
 * - Recibe el ID del usuario y su nombre a través de [Intent] extras.
 * - Observa LiveData del [DetallViewModel] para actualizar la UI en tiempo real.
 * - Muestra imagen de avatar usando Glide con placeholder y recorte circular.
 */
class DetallActivity : AppCompatActivity() {

    private val viewModel: DetallViewModel by viewModels()

    private lateinit var tvId: TextView
    private lateinit var tvNom: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutContingut: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detall)

        // ── Inicialización de views ────────────────────────────────
        tvId = findViewById(R.id.tvId)
        tvNom = findViewById(R.id.tvNom)
        tvEmail = findViewById(R.id.tvEmail)
        ivAvatar = findViewById(R.id.ivAvatar)
        progressBar = findViewById(R.id.progressBar)
        layoutContingut = findViewById(R.id.layoutContingut)

        // ── Recibir parámetros del Intent ─────────────────────────
        val userId = intent.getStringExtra("USER_ID")
        val userNom = intent.getStringExtra("USER_NOM") ?: "Usuari"

        if (userId == null) { finish(); return }

        // ── Configurar ActionBar ────────────────────────────────
        supportActionBar?.title = userNom
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ── Llamar al ViewModel para cargar usuario ──────────────
        viewModel.cargarUsuari(userId)

        // ── Observar LiveData del usuario ───────────────────────
        viewModel.usuari.observe(this) { user ->
            user?.let {
                tvId.text    = "ID: ${it.id}"
                tvNom.text   = "${it.nom} ${it.cognom}"
                tvEmail.text = it.email

                // ── Cargar avatar con Glide ─────────────────────
                Glide.with(this)
                    .load(it.avatar)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_person_placeholder)
                            .error(R.drawable.ic_person_placeholder)
                            .circleCrop()
                    )
                    .into(ivAvatar)
            }
        }

        // ── Observar estado de carga ───────────────────────────
        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            layoutContingut.visibility = if (loading) View.GONE else View.VISIBLE
        }

        // ── Observar errores ───────────────────────────────────
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