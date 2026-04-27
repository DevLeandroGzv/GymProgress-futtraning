package com.leandro.gymprogress_futtraing.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExerciseApiService {
    @GET("v1/exercises")
    suspend fun searchExercises(
        @Query("muscle") muscle: String? = null,
        @Query("name") name: String? = null,
        @Header("X-Api-Key") apiKey: String = "oeI4OYTQ5SFPu5bSKRtkJvBig6476yjx95KeqjzR" // Pon tu clave aquí
    ): List<ExerciseDto>

    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/"
    }
}