package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel per a la pantalla de detall d'un usuari.
 *
 * Funcions principals:
 * - Carregar un usuari des del repositori a partir de l'ID.
 * - Gestionar l'estat de càrrega i errors.
 *
 * LiveData exposades:
 * - [usuari]: l'usuari carregat (o null si no hi ha dades).
 * - [isLoading]: indica si s'està carregant informació.
 * - [error]: missatge d'error si hi ha algun problema en la càrrega.
 */
class DetallViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _usuari    = MutableLiveData<User?>()
    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()

    /** LiveData observable de l'usuari carregat. */
    val usuari: LiveData<User?> = _usuari

    /** LiveData observable per indicar l'estat de càrrega. */
    val isLoading: LiveData<Boolean> = _isLoading

    /** LiveData observable per mostrar errors. */
    val error: LiveData<String?> = _error

    /**
     * Carrega un usuari a partir del seu ID.
     *
     * @param id ID de l'usuari a carregar.
     *
     * Aquesta funció:
     * - Mostra la ProgressBar establint [_isLoading] a true.
     * - Reseteja [_error] a null.
     * - Crida al [UserRepository] per obtenir les dades.
     * - Si la resposta és correcta, actualitza [_usuari].
     * - Si hi ha un error, actualitza [_error] amb un missatge descriptiu.
     * - Sempre desactiva [_isLoading] al final.
     */
    fun cargarUsuari(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repo.getUsuari(id)
                if (response.isSuccessful) {
                    // MockAPI retorna directament l'usuari, sense envoltar en "data"
                    _usuari.value = response.body()
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