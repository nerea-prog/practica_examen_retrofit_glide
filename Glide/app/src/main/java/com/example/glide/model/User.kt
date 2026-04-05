package com.example.glide.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: String? = null,
    val email: String? = null,
    @SerializedName("first_name") val nom: String = "",
    @SerializedName("last_name") val cognom: String = "",
    val avatar: String? = null
)