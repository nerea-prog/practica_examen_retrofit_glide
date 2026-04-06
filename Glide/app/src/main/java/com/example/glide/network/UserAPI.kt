package com.example.glide.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee la instancia de Retrofit para la API de usuarios ([UsersService]).
 *
 * Este objeto asegura que solo exista una instancia de Retrofit en toda la aplicación.
 *
 * Características:
 * - `companion object` permite acceso estático a la API sin instanciar la clase.
 * - `@Synchronized` garantiza que la creación sea thread-safe.
 * - `baseUrl` debe terminar siempre con `/`.
 */
class UserAPI {
    companion object {
        /** Instancia única de [UsersService]. */
        private var instance: UsersService? = null

        /**
         * Retorna la instancia de la API de usuarios.
         *
         * Si aún no existe, se crea con Retrofit y Gson para manejar JSON.
         *
         * @return Instancia singleton de [UsersService].
         */
        @Synchronized
        fun API(): UsersService {
            if (instance == null) {
                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                instance = Retrofit.Builder()
                    .baseUrl("https://69d229a45043d95be971884b.mockapi.io/") // baseUrl termina en /
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(UsersService::class.java)
            }
            return instance!!
        }
    }
}