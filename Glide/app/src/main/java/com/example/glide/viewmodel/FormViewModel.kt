package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel per al formulari de creació i edició d'usuaris.
 *
 * Funcions principals:
 * - Crear un usuari nou (POST).
 * - Actualitzar un usuari existent (PUT).
 * - Gestionar l'estat de càrrega, errors i resultats.
 *
 * LiveData exposades:
 * - [isLoading]: indica si s'està realitzant una operació de xarxa.
 * - [error]: missatge d'error en cas de fallada.
 * - [missatge]: missatge d'èxit després d'una operació.
 * - [resultat]: informació detallada de l'usuari creat o actualitzat.
 */
class FormViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()
    private val _missatge  = MutableLiveData<String?>()
    private val _resultat  = MutableLiveData<String?>()

    /** LiveData observable que indica si hi ha una operació en curs. */
    val isLoading: LiveData<Boolean> = _isLoading

    /** LiveData observable per mostrar missatges d'error. */
    val error:     LiveData<String?> = _error

    /** LiveData observable per mostrar missatges d'èxit. */
    val missatge:  LiveData<String?> = _missatge

    /** LiveData observable amb informació de l'usuari creat o actualitzat. */
    val resultat:  LiveData<String?> = _resultat

    /**
     * Crea un nou usuari.
     *
     * @param nom Nom de l'usuari.
     * @param cognom Cognom de l'usuari.
     *
     * Aquesta funció:
     * - Mostra la ProgressBar establint [_isLoading] a true.
     * - Crida al [UserRepository] per crear un usuari.
     * - Si és exitós, actualitza [_missatge] i [_resultat].
     * - Si falla, actualitza [_error] amb el codi d'error o missatge de connexió.
     * - Sempre desactiva [_isLoading] al final.
     */
    fun crear(nom: String, cognom: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = User(nom = nom, cognom = cognom)
                val response = repo.crear(user)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari creat!"
                    _resultat.value = "ID: ${body.id}\nNom: ${body.nom} ${body.cognom}"
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

    /**
     * Actualitza un usuari existent.
     *
     * @param id ID de l'usuari a actualitzar.
     * @param nom Nou nom de l'usuari.
     * @param cognom Nou cognom de l'usuari.
     *
     * Aquesta funció:
     * - Mostra la ProgressBar establint [_isLoading] a true.
     * - Crida al [UserRepository] per actualitzar l'usuari.
     * - Si és exitós, actualitza [_missatge] i [_resultat].
     * - Si falla, actualitza [_error] amb el codi d'error o missatge de connexió.
     * - Sempre desactiva [_isLoading] al final.
     */
    fun actualitzar(id: String, nom: String, cognom: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = User(nom = nom, cognom = cognom)
                val response = repo.actualitzar(id, user)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari #$id actualitzat!"
                    _resultat.value = "Nom actualitzat: ${body.nom} ${body.cognom}"
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