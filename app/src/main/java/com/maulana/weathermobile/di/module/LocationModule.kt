package com.maulana.weathermobile.di.module


import com.maulana.weathermobile.data.datasource.local.AppDatabase
import com.maulana.weathermobile.data.datasource.local.LocationLocalDataSource
import com.maulana.weathermobile.data.datasource.local.LocationLocalDataSourceImpl
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSource
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSourceImpl
import com.maulana.weathermobile.data.datasource.remote.service.GeoCodingService
import com.maulana.weathermobile.data.repository.LocationRepositoryImpl
import com.maulana.weathermobile.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object LocationModule {

    @Provides
    fun provideLocationRepository(
        locationRemoteDataSource: LocationRemoteDataSource,
        locationLocalDataSource: LocationLocalDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(
            locationRemoteDataSource, locationLocalDataSource
        )
    }

    @Provides
    fun provideLocationRemoteDataSource(geoCodingService: GeoCodingService): LocationRemoteDataSource {
        return LocationRemoteDataSourceImpl(geoCodingService)
    }

    @Provides
    fun provideLocationLocalDataSource(appDatabase: AppDatabase): LocationLocalDataSource {
        return LocationLocalDataSourceImpl(appDatabase)
    }

    @Provides
    fun provideGeoCodingService(
        retrofit: Retrofit,
    ): GeoCodingService {
        return retrofit.create(GeoCodingService::class.java)
    }

}