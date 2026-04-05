package com.example.glide.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int = 0,

    val email: String = "",

    // @SerializedName → el camp JSON es diu "first_name" però a Kotlin li diem "nom"
    @SerializedName("first_name") val nom: String = "",

    @SerializedName("last_name") val cognom: String = "",

    // avatar → URL d'imatge. Glide la carregarà a un ImageView
    val avatar: String = ""
)