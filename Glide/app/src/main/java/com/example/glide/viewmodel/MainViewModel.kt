package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel principal per a la llista d'usuaris.
 *
 * Funcions principals:
 * - Carregar la llista d'usuaris (GET).
 * - Eliminar un usuari (DELETE).
 * - Gestionar estats de càrrega, errors i missatges d'èxit.
 *
 * LiveData exposades:
 * - [llista]: la llista actual d'usuaris.
 * - [isLoading]: indica si s'està realitzant una operació de xarxa.
 * - [error]: missatge d'error en cas de fallada.
 * - [missatge]: missatge d'èxit després d'una operació.
 */
class MainViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _llista    = MutableLiveData<List<User>>()
    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()
    private val _missatge  = MutableLiveData<String?>()

    /** LiveData observable amb la llista d'usuaris actual. */
    val llista:    LiveData<List<User>>  = _llista

    /** LiveData observable que indica si hi ha una operació en curs. */
    val isLoading: LiveData<Boolean>     = _isLoading

    /** LiveData observable per mostrar missatges d'error. */
    val error:     LiveData<String?>     = _error

    /** LiveData observable per mostrar missatges d'èxit. */
    val missatge:  LiveData<String?>     = _missatge

    /** Inicialitza el ViewModel carregant la llista d'usuaris. */
    init { cargar() }

    /**
     * Carrega la llista d'usuaris des del [UserRepository].
     *
     * Aquesta funció:
     * - Estableix [_isLoading] a true mentre es fa la crida.
     * - Si és exitosa, actualitza [_llista] amb la resposta.
     * - Si falla, actualitza [_error] amb un missatge adequat.
     * - Sempre desactiva [_isLoading] al final.
     */
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

    /**
     * Elimina un usuari concret segons el seu [id].
     *
     * Aquesta funció:
     * - Crida al [UserRepository] per eliminar l'usuari.
     * - Si és exitosa, mostra un missatge d'èxit i elimina l'usuari de [_llista].
     * - Si falla, mostra un missatge amb el codi d'error o missatge de connexió.
     *
     * @param id ID de l'usuari a eliminar.
     */
    fun eliminar(id: String) {
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

    /**
     * Neteja el missatge d'èxit actual.
     *
     * S'utilitza normalment després de mostrar un Toast a la UI.
     */
    fun netejarMissatge() { _missatge.value = null }
}