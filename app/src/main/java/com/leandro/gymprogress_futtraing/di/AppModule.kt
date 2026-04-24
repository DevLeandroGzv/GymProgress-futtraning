package com.leandro.gymprogress_futtraing.di

import android.app.Application
import androidx.room.Room
import com.leandro.gymprogress_futtraing.data.GymDatabase
import com.leandro.gymprogress_futtraing.domain.repository.ExerciseRepository
import com.leandro.gymprogress_futtraing.data.repository.ExcerciseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}