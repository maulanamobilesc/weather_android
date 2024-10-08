package com.maulana.weathermobile.di.module


import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSource
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSourceImpl
import com.maulana.weathermobile.data.repository.LocationRepositoryImpl
import com.maulana.weathermobile.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object LocationModule {

    @Provides
    fun provideLocationRepository(
        locationRemoteDataSource: LocationRemoteDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(
            locationRemoteDataSource
        )
    }


    @Provides
    fun provideLocationRemoteDataSource(): LocationRemoteDataSource {
        return LocationRemoteDataSourceImpl()
    }

}