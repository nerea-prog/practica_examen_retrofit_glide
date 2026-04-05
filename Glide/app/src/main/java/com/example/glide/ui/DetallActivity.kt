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

        tvId = findViewById(R.id.tvId)
        tvNom = findViewById(R.id.tvNom)
        tvEmail = findViewById(R.id.tvEmail)
        ivAvatar = findViewById(R.id.ivAvatar)
        progressBar = findViewById(R.id.progressBar)
        layoutContingut = findViewById(R.id.layoutContingut)

        val userId  = intent.getIntExtra("USER_ID", -1)
        val userNom = intent.getStringExtra("USER_NOM") ?: "Usuari #$userId"

        if (userId == -1) { finish(); return }

        supportActionBar?.title = userNom
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Cridar el ViewModel amb l'ID rebut
        viewModel.cargarUsuari(userId)

        // ── Observar LiveData ─────────────────────────────────────────────
        viewModel.usuari.observe(this) { user ->
            user?.let {
                tvId.text     = "ID: ${it.id}"
                tvNom.text    = "${it.nom} ${it.cognom}"
                tvEmail.text  = it.email

                // ── GLIDE a la pantalla de detall ─────────────────────────
                // Imatge més gran, també circular
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

        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            layoutContingut.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
