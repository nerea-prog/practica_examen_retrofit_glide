package com.example.glide.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * SINGLETON RETROFIT
 *
 * - companion object → accés estàtic sense instanciar la classe
 * - @Synchronized → thread-safe (evita crear dos objectes alhora)
 * - baseUrl SEMPRE acaba amb /
 */
class UserAPI {
    companion object {
        private var instance: UsersService? = null

        @Synchronized
        fun API(): UsersService {
            if (instance == null) {
                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                instance = Retrofit.Builder()
                    .baseUrl("https://69d229a45043d95be971884b.mockapi.io/")          // ← acaba amb /
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(UsersService::class.java)
            }
            return instance!!
        }
    }
}