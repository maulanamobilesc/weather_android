package com.maulana.weathermobile.di.module

import android.app.Application
import com.maulana.weathermobile.data.datasource.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RoomModule {

    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }

}