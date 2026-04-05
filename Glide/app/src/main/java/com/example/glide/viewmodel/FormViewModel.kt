package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

class FormViewModel : ViewModel() {

    private val repo = UserRepository()

    private val _isLoading = MutableLiveData(false)
    private val _error     = MutableLiveData<String?>()
    private val _missatge  = MutableLiveData<String?>()
    private val _resultat  = MutableLiveData<String?>()

    val isLoading: LiveData<Boolean> = _isLoading
    val error:     LiveData<String?> = _error
    val missatge:  LiveData<String?> = _missatge
    val resultat:  LiveData<String?> = _resultat

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
