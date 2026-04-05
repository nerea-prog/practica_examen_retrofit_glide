package com.example.glideretrofitdemo.viewmodel

import androidx.lifecycle.*
import com.example.glideretrofitdemo.model.UserRequest
import com.example.glideretrofitdemo.repository.UserRepository
import kotlinx.coroutines.launch

class FormViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()
    private val _missatge  = MutableLiveData<String?>()
    // Guardem el resultat com a String per mostrar-lo a la UI
    private val _resultat  = MutableLiveData<String?>()

    val isLoading: LiveData<Boolean> = _isLoading
    val error:     LiveData<String?> = _error
    val missatge:  LiveData<String?> = _missatge
    val resultat:  LiveData<String?> = _resultat

    // ── CAS 4: POST — Crear usuari nou ────────────────────────────────────
    fun crear(nom: String, feina: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = UserRequest(name = nom, job = feina)
                val response = repo.crear(request)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari creat!"
                    _resultat.value = """
                        ✅ Resposta POST (HTTP 201):
                        ID generat: ${body.id}
                        Nom: ${body.name}
                        Feina: ${body.job}
                        Creat el: ${body.createdAt}
                    """.trimIndent()
                } else {
                    _error.value = "Error POST: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── CAS 5: PUT — Actualitzar usuari existent ──────────────────────────
    fun actualitzar(id: Int, nom: String, feina: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = UserRequest(name = nom, job = feina)
                val response = repo.actualitzar(id, request)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari #$id actualitzat!"
                    _resultat.value = """
                        ✅ Resposta PUT (HTTP 200):
                        Nom actualitzat: ${body.name}
                        Feina actualitzada: ${body.job}
                        Actualitzat el: ${body.updatedAt}
                    """.trimIndent()
                } else {
                    _error.value = "Error PUT: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
