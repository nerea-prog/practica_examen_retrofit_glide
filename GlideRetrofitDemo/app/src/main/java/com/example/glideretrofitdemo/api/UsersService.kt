package com.example.glideretrofitdemo.api

import com.example.glideretrofitdemo.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * INTERFÍCIE — tots els endpoints de reqres.in
 *
 * Base URL: https://reqres.in/
 *
 * Endpoints reals que usem:
 *   GET    api/users?page=1     → llista paginada amb avatars
 *   GET    api/users/{id}       → un sol usuari
 *   POST   api/users            → crear (retorna id + createdAt)
 *   PUT    api/users/{id}       → actualitzar (retorna updatedAt)
 *   DELETE api/users/{id}       → eliminar (retorna HTTP 204)
 */
interface UsersService {

    // ── CAS 1: GET llista (pagina=1 té 6 usuaris amb avatars reals) ────────
    @GET("api/users")
    suspend fun getLlista(
        @Query("page") page: Int = 1
    ): Response<UsersResponse>

    // ── CAS 2: GET per ID ─────────────────────────────────────────────────
    @GET("api/users/{id}")
    suspend fun getUsuari(
        @Path("id") id: Int
    ): Response<UserResponse>

    // ── CAS 4: POST — crear usuari nou ────────────────────────────────────
    @POST("api/users")
    suspend fun crearUsuari(
        @Body userRequest: UserRequest
    ): Response<CreateResponse>

    // ── CAS 5: PUT — actualitzar usuari existent ──────────────────────────
    @PUT("api/users/{id}")
    suspend fun actualitzarUsuari(
        @Path("id") id: Int,
        @Body userRequest: UserRequest
    ): Response<UpdateResponse>

    // ── CAS 6: DELETE — eliminar usuari ───────────────────────────────────
    @DELETE("api/users/{id}")
    suspend fun eliminarUsuari(
        @Path("id") id: Int
    ): Response<Unit>
}
