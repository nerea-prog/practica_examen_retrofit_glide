package com.example.glide.network

import com.example.glide.model.User
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit que define los endpoints para manejar usuarios ([User]).
 *
 * Todos los métodos son `suspend` para ser usados con coroutines.
 */
interface UsersService {

    /**
     * Obtiene la lista completa de usuarios.
     *
     * @return [Response] que contiene la lista de [User].
     */
    @GET("users")
    suspend fun getLlista(): Response<List<User>>

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario a recuperar.
     * @return [Response] que contiene el [User] solicitado.
     */
    @GET("users/{id}")
    suspend fun getUsuari(@Path("id") id: String): Response<User>

    /**
     * Crea un nuevo usuario en el servidor.
     *
     * @param user Objeto [User] con los datos del usuario a crear.
     * @return [Response] que contiene el usuario creado con ID asignado.
     */
    @POST("users")
    suspend fun crearUsuari(@Body user: User): Response<User>

    /**
     * Actualiza un usuario existente por su ID.
     *
     * @param id ID del usuario a actualizar.
     * @param user Objeto [User] con los nuevos datos.
     * @return [Response] que contiene el usuario actualizado.
     */
    @PUT("users/{id}")
    suspend fun actualitzarUsuari(@Path("id") id: String, @Body user: User): Response<User>

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return [Response] unitario; solo indica éxito o error.
     */
    @DELETE("users/{id}")
    suspend fun eliminarUsuari(@Path("id") id: String): Response<Unit>
}