package com.example.glideretrofitdemo.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.glideretrofitdemo.adapter.UserAdapter
import com.example.glideretrofitdemo.databinding.ActivityMainBinding
import com.example.glideretrofitdemo.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarRecyclerView()
        observarViewModel()

        binding.btnNou.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
        binding.btnRecarregar.setOnClickListener {
            viewModel.cargar()
        }
    }

    private fun configurarRecyclerView() {
        adapter = UserAdapter(
            onVeure = { user ->
                // CAS 2: passa l'ID a DetallActivity
                Intent(this, DetallActivity::class.java).also {
                    it.putExtra("USER_ID", user.id)
                    it.putExtra("USER_NOM", "${user.nom} ${user.cognom}")
                    startActivity(it)
                }
            },
            onEditar = { user ->
                // CAS 5: passa totes les dades a FormActivity
                Intent(this, FormActivity::class.java).also {
                    it.putExtra("USER_ID", user.id)
                    it.putExtra("USER_NOM", "${user.nom} ${user.cognom}")
                    it.putExtra("USER_JOB", "engineer")
                    startActivity(it)
                }
            },
            onEliminar = { id ->
                // CAS 6: confirmació i DELETE
                AlertDialog.Builder(this)
                    .setTitle("Eliminar usuari")
                    .setMessage("Segur que vols eliminar l'usuari #$id?")
                    .setPositiveButton("Sí, eliminar") { _, _ -> viewModel.eliminar(id) }
                    .setNegativeButton("Cancel·lar", null)
                    .show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observarViewModel() {
        // CAS 1: llista actualitzada
        viewModel.llista.observe(this) { llista ->
            adapter.update(llista)
            binding.tvInfo.text = "${llista.size} usuaris carregats de reqres.in"
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.recyclerView.visibility  = if (loading) View.GONE  else View.VISIBLE
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
