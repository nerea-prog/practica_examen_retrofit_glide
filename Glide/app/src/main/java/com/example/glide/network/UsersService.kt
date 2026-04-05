package com.example.glide.network

import com.example.glide.model.User
import retrofit2.Response
import retrofit2.http.*

interface UsersService {

    @GET("users")
    suspend fun getLlista(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUsuari(@Path("id") id: String): Response<User>

    @POST("users")
    suspend fun crearUsuari(@Body user: User): Response<User>

    @PUT("users/{id}")
    suspend fun actualitzarUsuari(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE("users/{id}")
    suspend fun eliminarUsuari(@Path("id") id: String): Response<Unit>
}
