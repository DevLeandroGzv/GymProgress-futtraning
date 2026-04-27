package com.leandro.gymprogress_futtraing.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.leandro.gymprogress_futtraing.data.GymDatabase
import com.leandro.gymprogress_futtraing.data.remote.ExerciseApiService
import com.leandro.gymprogress_futtraing.domain.repository.ExerciseRepository
import com.leandro.gymprogress_futtraing.data.repository.ExcerciseRepositoryImpl
import com.leandro.gymprogress_futtraing.data.repository.ExternalExerciseRepositoryImpl
import com.leandro.gymprogress_futtraing.domain.repository.ExternalExerciseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGymDatabase(app: Application): GymDatabase {
        return Room.databaseBuilder(
            app,
            GymDatabase::class.java,
            "gym_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(db: GymDatabase): ExerciseRepository {

        return ExcerciseRepositoryImpl(db.exerciseDao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("gym_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ExerciseApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseApiService(retrofit: Retrofit): ExerciseApiService {
        return retrofit.create(ExerciseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExternalRepository(apiService: ExerciseApiService): ExternalExerciseRepository {
        return ExternalExerciseRepositoryImpl(apiService)
    }
}