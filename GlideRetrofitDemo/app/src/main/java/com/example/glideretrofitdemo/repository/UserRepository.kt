package com.example.glideretrofitdemo.repository

import com.example.glideretrofitdemo.api.UserAPI
import com.example.glideretrofitdemo.model.UserRequest

class UserRepository {
    suspend fun getLlista(page: Int = 1) = UserAPI.API().getLlista(page)
    suspend fun getUsuari(id: Int)       = UserAPI.API().getUsuari(id)
    suspend fun crear(req: UserRequest)  = UserAPI.API().crearUsuari(req)
    suspend fun actualitzar(id: Int, req: UserRequest) = UserAPI.API().actualitzarUsuari(id, req)
    suspend fun eliminar(id: Int)        = UserAPI.API().eliminarUsuari(id)
}
