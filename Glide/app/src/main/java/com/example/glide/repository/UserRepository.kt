package com.example.glide.repository

import com.example.glide.model.User
import com.example.glide.network.UserAPI

class UserRepository {
    suspend fun getLlista() = UserAPI.API().getLlista()
    suspend fun getUsuari(id: String) = UserAPI.API().getUsuari(id)
    suspend fun crear(user: User) = UserAPI.API().crearUsuari(user)
    suspend fun actualitzar(id: String, user: User) = UserAPI.API().actualitzarUsuari(id, user)
    suspend fun eliminar(id: String) = UserAPI.API().eliminarUsuari(id)
}
