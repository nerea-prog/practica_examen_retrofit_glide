package com.example.glide.repository

import com.example.glide.model.UserRequest
import com.example.glide.network.UserAPI

class UserRepository {
    suspend fun getLlista() = UserAPI.API().getLlista()
    suspend fun getUsuari(id: Int) = UserAPI.API().getUsuari(id)
    suspend fun crear(req: UserRequest) = UserAPI.API().crearUsuari(req)
    suspend fun actualitzar(id: Int, req: UserRequest) = UserAPI.API().actualitzarUsuari(id, req)
    suspend fun eliminar(id: Int) = UserAPI.API().eliminarUsuari(id)
}
