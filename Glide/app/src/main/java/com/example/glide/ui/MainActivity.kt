package com.example.glide.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.glide.R
import com.example.glide.adapter.UserAdapter
import com.example.glide.viewmodel.MainViewModel

/**
 * Activity principal que muestra la lista de usuarios.
 *
 * Funcionalidades:
 * - Mostrar usuarios en un RecyclerView usando [UserAdapter].
 * - Permitir crear un nuevo usuario (navegando a [FormActivity]).
 * - Permitir editar un usuario existente.
 * - Permitir eliminar un usuario con confirmación.
 * - Observar LiveData de [MainViewModel] para actualizar la UI automáticamente.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvInfo: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ── Inicializar views ───────────────────────────────
        recyclerView = findViewById(R.id.recyclerView)
        tvInfo = findViewById(R.id.tvInfo)
        progressBar = findViewById(R.id.progressBar)

        val btnNou = findViewById<Button>(R.id.btnNou)
        val btnRecarregar = findViewById<Button>(R.id.btnRecarregar)

        configurarRecyclerView()
        observarViewModel()

        // ── Botón para crear nuevo usuario ─────────────────
        btnNou.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }

        // ── Botón para recargar la lista ──────────────────
        btnRecarregar.setOnClickListener {
            viewModel.cargar()
        }
    }

    /**
     * Configura el RecyclerView y su adapter con lambdas para:
     * - Ver detalles de un usuario.
     * - Editar un usuario.
     * - Eliminar un usuario con confirmación.
     */
    private fun configurarRecyclerView() {
        adapter = UserAdapter(
            onVeure = { user ->
                Intent(this, DetallActivity::class.java).also {
                    it.putExtra("USER_ID", user.id)
                    it.putExtra("USER_NOM", "${user.nom} ${user.cognom}")
                    startActivity(it)
                }
            },
            onEditar = { user ->
                Intent(this, FormActivity::class.java).also {
                    it.putExtra("USER_ID", user.id)
                    it.putExtra("USER_NOM", "${user.nom} ${user.cognom}")
                    it.putExtra("USER_JOB", "engineer") // Ejemplo de job
                    startActivity(it)
                }
            },
            onEliminar = { id ->
                AlertDialog.Builder(this)
                    .setTitle("Eliminar usuari")
                    .setMessage("Segur que vols eliminar l'usuari #$id?")
                    .setPositiveButton("Sí, eliminar") { _, _ -> viewModel.eliminar(id) }
                    .setNegativeButton("Cancel·lar", null)
                    .show()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    /**
     * Observa LiveData del [MainViewModel] para actualizar la UI:
     * - [llista]: actualiza el RecyclerView y el contador de usuarios.
     * - [isLoading]: muestra u oculta la ProgressBar y el RecyclerView.
     * - [error]: muestra un Toast con el error.
     * - [missatge]: muestra un Toast con mensajes y limpia el mensaje en el ViewModel.
     */
    private fun observarViewModel() {
        viewModel.llista.observe(this) { llista ->
            adapter.update(llista)
            tvInfo.text = "${llista.size} usuaris carregats"
        }

        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            recyclerView.visibility  = if (loading) View.GONE  else View.VISIBLE
        }

        viewModel.error.observe(this) { err ->
            err?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }

        viewModel.missatge.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.netejarMissatge()
            }
        }
    }
}