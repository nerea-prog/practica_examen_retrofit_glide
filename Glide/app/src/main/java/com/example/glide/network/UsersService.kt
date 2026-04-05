package com.example.glide.network

import com.example.glide.model.CreateResponse
import com.example.glide.model.UpdateResponse
import com.example.glide.model.UserRequest
import com.example.glide.model.User
import retrofit2.Response
import retrofit2.http.*

interface UsersService {

    @GET("users")
    suspend fun getLlista(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUsuari(
        @Path("id") id: Int
    ): Response<User>

    @POST("users")
    suspend fun crearUsuari(
        @Body userRequest: UserRequest
    ): Response<CreateResponse>

    @PUT("users/{id}")
    suspend fun actualitzarUsuari(
        @Path("id") id: Int,
        @Body userRequest: UserRequest
    ): Response<UpdateResponse>

    @DELETE("users/{id}")
    suspend fun eliminarUsuari(
        @Path("id") id: Int
    ): Response<Unit>
}
