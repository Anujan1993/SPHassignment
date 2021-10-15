package com.anujan.sphassignment.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.anujan.sphassignment.app.SPHApplication
import com.anujan.sphassignment.dao.AppUserDao
import com.anujan.sphassignment.dao.MainRecordsDao
import com.anujan.sphassignment.util.AppDatabase
import com.anujan.sphassignment.util.EndPoints
import com.anujan.sphassignment.util.SharedPreferencesData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule(var context: SPHApplication) {

    @Provides
    fun provideApplicationContext(): Context {
        return context
    }

    @Provides
    open fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPreferencesData.APP_USER, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    open fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, EndPoints.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    open fun providesAppUserDao(database: AppDatabase): AppUserDao {
        return database.appUserDao()
    }


    @Singleton
    @Provides
    open fun providesMainRecordsDao(database: AppDatabase): MainRecordsDao {
        return database.mainRecordsDao()
    }
}