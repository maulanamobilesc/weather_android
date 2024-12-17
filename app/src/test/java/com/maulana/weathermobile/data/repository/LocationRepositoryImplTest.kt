package com.maulana.weathermobile.data.repository

import android.content.Context
import com.maulana.warehouse.domain.UIState
import com.maulana.weathermobile.data.datasource.local.LocationLocalDataSource
import com.maulana.weathermobile.data.datasource.remote.LocationRemoteDataSource
import com.maulana.weathermobile.domain.model.LocationLocal
import com.maulana.weathermobile.domain.repository.LocationRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class LocationRepositoryImplTest {

    private lateinit var repository: LocationRepository
    private val remoteDataSource: LocationRemoteDataSource = mock()
    private val localDataSource: LocationLocalDataSource = mock()

    @BeforeEach
    fun setUp() {
        repository = LocationRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getCurrentLocation should return error state when remote call fails`() = runTest {
        // Given
        val context: Context = mock()
        val errorState = UIState.Error("Unable to fetch location")
        `when`(remoteDataSource.getCurrentLocation(context)).thenReturn(flowOf(errorState))

        // When
        val result = repository.getCurrentLocation(context)

        // Then
        result.collect { state ->
            assertEquals(errorState, state)
        }
    }

    @Test
    fun `getAllSavedLocation should return flow of location list`() = runTest {
        // Given
        val expectedLocations = listOf(
            LocationLocal(1, "Jakarta", -6.2088, 106.8456, true, "DKI Jakarta", "Indonesia"),
            LocationLocal(2, "Bandung", -6.9147, 107.6098, false, "West Java", "Indonesia")
        )
        `when`(localDataSource.getAllSavedLocation()).thenReturn(flowOf(expectedLocations))

        // When
        val result = repository.getAllSavedLocation()

        // Then
        result.collect { locations ->
            assertEquals(expectedLocations, locations)
        }
    }

    @Test
    fun `insertLocation should call localDataSource insertLocation`() = runTest {
        // Given
        val location =
            LocationLocal(3, "Surabaya", -7.2575, 112.7521, false, "East Java", "Indonesia")

        // When
        repository.insertLocation(location)

        // Then
        Mockito.verify(localDataSource).insertLocation(location)
    }

    @Test
    fun `insertLocation should handle duplicate location insertion`() = runTest {
        // Given
        val duplicateLocation =
            LocationLocal(1, "Jakarta", -6.2088, 106.8456, true, "DKI Jakarta", "Indonesia")
        `when`(localDataSource.insertLocation(duplicateLocation)).thenThrow(IllegalStateException("Duplicate entry"))

        // When/Then
        assertThrows<IllegalStateException> {
            repository.insertLocation(duplicateLocation)
        }
    }


    @Test
    fun `searchLocation should return flow of UIState with location list`() = runTest {
        // Given
        val query = "Bali"
        val apiKey = "testApiKey"
        val expectedLocations = listOf(
            LocationLocal(4, "Denpasar", -8.6705, 115.2126, false, "Bali", "Indonesia")
        )
        `when`(remoteDataSource.searchLocation(query, apiKey)).thenReturn(
            flowOf(
                UIState.Success(
                    expectedLocations
                )
            )
        )

        // When
        val result = repository.searchLocation(query, apiKey)

        // Then
        result.collect { state ->
            assertEquals(UIState.Success(expectedLocations), state)
        }
    }

    @Test
    fun `searchLocation should return empty list when no locations match the query`() = runTest {
        // Given
        val query = "Unknown Place"
        val apiKey = "testApiKey"
        `when`(remoteDataSource.searchLocation(query, apiKey)).thenReturn(
            flowOf(
                UIState.Success(
                    emptyList()
                )
            )
        )

        // When
        val result = repository.searchLocation(query, apiKey)

        // Then
        result.collect { state ->
            assertEquals(UIState.Success(emptyList<LocationLocal>()), state)
        }
    }

}
