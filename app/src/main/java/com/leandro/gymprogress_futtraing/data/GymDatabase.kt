package com.leandro.gymprogress_futtraing.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leandro.gymprogress_futtraing.data.local.dao.ExerciseDao
import com.leandro.gymprogress_futtraing.data.local.entities.ExerciseEntity

@Database(entities = [ExerciseEntity::class], version = 1)
abstract class GymDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
}