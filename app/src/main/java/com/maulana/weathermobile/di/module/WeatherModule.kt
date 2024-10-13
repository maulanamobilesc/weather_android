package com.maulana.weathermobile.di.module

import com.maulana.weathermobile.data.datasource.local.AppDatabase
import com.maulana.weathermobile.data.datasource.local.WeatherLocalDataSource
import com.maulana.weathermobile.data.datasource.local.WeatherLocalDataSourceImpl
import com.maulana.weathermobile.data.datasource.remote.WeatherRemoteDataSource
import com.maulana.weathermobile.data.datasource.remote.WeatherRemoteDataSourceImpl
import com.maulana.weathermobile.data.datasource.remote.service.WeatherService
import com.maulana.weathermobile.data.repository.WeatherRepositoryImpl
import com.maulana.weathermobile.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object WeatherModule {

    @Provides
    fun provideWeatherRepository(
        weatherRemoteDataSource: WeatherRemoteDataSource,
        weatherLocalDataSource: WeatherLocalDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            weatherRemoteDataSource, weatherLocalDataSource
        )
    }


    @Provides
    fun provideWeatherRemoteDataSource(
        weatherService: WeatherService,
    ): WeatherRemoteDataSource {
        return WeatherRemoteDataSourceImpl(weatherService)
    }

    @Provides
    fun provideWeatherLocalDataSource(appDatabase: AppDatabase): WeatherLocalDataSource {
        return WeatherLocalDataSourceImpl(appDatabase)
    }

    @Provides
    fun provideWeatherService(
        retrofit: Retrofit,
    ): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

}