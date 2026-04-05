package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _llista    = MutableLiveData<List<User>>()
    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()
    private val _missatge  = MutableLiveData<String?>()

    val llista:    LiveData<List<User>>  = _llista
    val isLoading: LiveData<Boolean>     = _isLoading
    val error:     LiveData<String?>     = _error
    val missatge:  LiveData<String?>     = _missatge

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repo.getLlista()
                if (response.isSuccessful) {
                    _llista.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error HTTP ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Sense connexió: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            try {
                val response = repo.eliminar(id)
                if (response.isSuccessful) {
                    _missatge.value = "Usuari #$id eliminat ✓"
                    _llista.value = _llista.value?.filter { it.id != id }
                } else {
                    _missatge.value = "Error al eliminar: ${response.code()}"
                }
            } catch (e: Exception) {
                _missatge.value = "Error: ${e.message}"
            }
        }
    }

    fun netejarMissatge() { _missatge.value = null }
}
