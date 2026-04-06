package com.example.glide.viewmodel

import androidx.lifecycle.*
import com.example.glide.model.User
import com.example.glide.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de formulario.
 *
 * Se encarga de la lógica de creación (POST) y actualización (PUT) de usuarios.
 * Maneja el envío de datos incluyendo el nombre, apellido y URL del avatar.
 */
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

    /**
     * Crea un nuevo usuario.
     *
     * @param nom Nombre del usuario.
     * @param cognom Apellido del usuario.
     * @param avatar URL de la imagen del avatar.
     */
    fun crear(nom: String, cognom: String, avatar: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Ahora enviamos también la URL del avatar en el objeto User
                val user = User(nom = nom, cognom = cognom, avatar = avatar)
                val response = repo.crear(user)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari creat!"
                    _resultat.value = "ID: ${body.id}\nNom: ${body.nom} ${body.cognom}\nAvatar: ${body.avatar}"
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
     * Actualiza un usuario existente.
     *
     * @param id ID del usuario a editar.
     * @param nom Nuevo nombre.
     * @param cognom Nuevo apellido.
     * @param avatar Nueva URL del avatar.
     */
    fun actualitzar(id: String, nom: String, cognom: String, avatar: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Enviamos los datos actualizados incluyendo el avatar
                val user = User(nom = nom, cognom = cognom, avatar = avatar)
                val response = repo.actualitzar(id, user)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _missatge.value = "Usuari #$id actualitzat!"
                    _resultat.value = "Nom actualitzat: ${body.nom} ${body.cognom}\nAvatar: ${body.avatar}"
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
