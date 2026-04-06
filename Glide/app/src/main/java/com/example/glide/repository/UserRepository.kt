package com.example.glide.repository

import com.example.glide.model.User
import com.example.glide.network.UserAPI

/**
 * Repositorio que actúa como capa de abstracción sobre la API de usuarios ([UserAPI]).
 *
 * Permite acceder a los datos de los usuarios sin exponer directamente la implementación de la red.
 * Todos los métodos son `suspend` y se deben llamar desde coroutines o ViewModelScope.
 */
class UserRepository {

    /**
     * Obtiene la lista completa de usuarios.
     *
     * @return [Response] que contiene la lista de [User].
     */
    suspend fun getLlista() = UserAPI.API().getLlista()

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario a recuperar.
     * @return [Response] que contiene el [User] solicitado.
     */
    suspend fun getUsuari(id: String) = UserAPI.API().getUsuari(id)

    /**
     * Crea un nuevo usuario en el servidor.
     *
     * @param user Objeto [User] con los datos a crear.
     * @return [Response] que contiene el usuario creado con ID asignado.
     */
    suspend fun crear(user: User) = UserAPI.API().crearUsuari(user)

    /**
     * Actualiza un usuario existente por su ID.
     *
     * @param id ID del usuario a actualizar.
     * @param user Objeto [User] con los nuevos datos.
     * @return [Response] que contiene el usuario actualizado.
     */
    suspend fun actualitzar(id: String, user: User) = UserAPI.API().actualitzarUsuari(id, user)

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return [Response] unitario; solo indica éxito o error.
     */
    suspend fun eliminar(id: String) = UserAPI.API().eliminarUsuari(id)
}