package com.example.glideretrofitdemo.model

import com.google.gson.annotations.SerializedName

// ── JSON que retorna reqres.in per a la LLISTA ─────────────────────────────
// {
//   "data": [
//     { "id":1, "email":"george.bluth@reqres.in",
//       "first_name":"George", "last_name":"Bluth",
//       "avatar":"https://reqres.in/img/faces/1-image.jpg" }
//   ]
// }
data class UsersResponse(
    val data: List<User>
)

// ── JSON que retorna reqres.in per a UN USUARI ─────────────────────────────
// { "data": { "id":2, "email":"...", "first_name":"...", ... } }
data class UserResponse(
    val data: User
)

// ── Model principal d'usuari ──────────────────────────────────────────────
data class User(
    val id: Int = 0,

    val email: String = "",

    // @SerializedName → el camp JSON es diu "first_name" però a Kotlin li diem "nom"
    @SerializedName("first_name") val nom: String = "",

    @SerializedName("last_name") val cognom: String = "",

    // avatar → URL d'imatge. Glide la carregarà a un ImageView
    val avatar: String = ""
)

// ── Model per a POST/PUT (reqres accepta name + job) ─────────────────────
// Quan CREEM o ACTUALITZEM, l'API de reqres.in espera aquest format:
// { "name": "Joan", "job": "developer" }
data class UserRequest(
    val name: String,
    val job: String
)

// ── Resposta de POST (crear) ──────────────────────────────────────────────
// { "name":"Joan", "job":"dev", "id":"101", "createdAt":"2024-..." }
data class CreateResponse(
    val name: String,
    val job: String,
    val id: String,
    val createdAt: String
)

// ── Resposta de PUT (actualitzar) ─────────────────────────────────────────
// { "name":"Joan", "job":"dev", "updatedAt":"2024-..." }
data class UpdateResponse(
    val name: String,
    val job: String,
    val updatedAt: String
)
