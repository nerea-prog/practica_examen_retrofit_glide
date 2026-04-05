package com.example.glideretrofitdemo.viewmodel

import androidx.lifecycle.*
import com.example.glideretrofitdemo.model.User
import com.example.glideretrofitdemo.repository.UserRepository
import kotlinx.coroutines.launch

class DetallViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _usuari    = MutableLiveData<User?>()
    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()

    val usuari:    LiveData<User?>   = _usuari
    val isLoading: LiveData<Boolean> = _isLoading
    val error:     LiveData<String?> = _error

    // ── CAS 2: GET per ID ─────────────────────────────────────────────────
    fun cargarUsuari(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repo.getUsuari(id)
                if (response.isSuccessful) {
                    _usuari.value = response.body()?.data   // UN sol objecte
                } else {
                    _error.value = "Usuari no trobat (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de connexió: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
