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

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvInfo: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        tvInfo = findViewById(R.id.tvInfo)
        progressBar = findViewById(R.id.progressBar)

        val btnNou = findViewById<Button>(R.id.btnNou)
        val btnRecarregar = findViewById<Button>(R.id.btnRecarregar)

        configurarRecyclerView()
        observarViewModel()

        btnNou.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
        btnRecarregar.setOnClickListener {
            viewModel.cargar()
        }
    }

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
                    it.putExtra("USER_JOB", "engineer")
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
