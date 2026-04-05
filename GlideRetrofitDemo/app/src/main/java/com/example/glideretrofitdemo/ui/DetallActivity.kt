package com.example.glideretrofitdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.glideretrofitdemo.R
import com.example.glideretrofitdemo.databinding.ActivityDetallBinding
import com.example.glideretrofitdemo.viewmodel.DetallViewModel

class DetallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallBinding
    private val viewModel: DetallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                binding.tvId.text     = "ID: ${it.id}"
                binding.tvNom.text    = "${it.nom} ${it.cognom}"
                binding.tvEmail.text  = it.email

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
                    .into(binding.ivAvatar)
            }
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.layoutContingut.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
