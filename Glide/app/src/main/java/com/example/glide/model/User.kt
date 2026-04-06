package com.example.glide.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa un usuario.
 *
 * Esta clase se utiliza para deserializar la respuesta JSON de la API de usuarios
 * y para mostrar información en la UI.
 *
 * @property id ID único del usuario. Puede ser nulo si no se ha asignado aún.
 * @property email Correo electrónico del usuario. Puede ser nulo.
 * @property nom Nombre del usuario. Mapeado desde el campo JSON `first_name`.
 * @property cognom Apellido del usuario. Mapeado desde el campo JSON `last_name`.
 * @property avatar URL de la imagen de avatar del usuario. Puede ser nula.
 */
data class User(
    val id: String? = null,
    val email: String? = null,
    @SerializedName("first_name") val nom: String = "",
    @SerializedName("last_name") val cognom: String = "",
    val avatar: String? = null
)