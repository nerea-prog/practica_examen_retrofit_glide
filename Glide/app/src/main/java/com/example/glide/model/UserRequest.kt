package com.example.glide.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("first_name") val name: String,
    @SerializedName("last_name") val job: String
)
