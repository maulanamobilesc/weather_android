package com.maulana.weathermobile.data.datasource.local

import android.content.Context
import androidx.room.Room
import com.maulana.weathermobile.data.datasource.local.dao.LocationDao
import com.maulana.weathermobile.data.datasource.local.dao.WeatherDao
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.mockito.Mockito.mock
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppDatabaseTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var locationDao: LocationDao
    private lateinit var weatherDao: WeatherDao
    private val context: Context = mock(Context::class.java)

    @BeforeAll
    fun setUp() {
        // Create in-memory database for testing purposes
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        locationDao = appDatabase.locationDao()
        weatherDao = appDatabase.weatherDao()
    }

    @AfterAll
    @Throws(IOException::class)
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun `database instance is created`() {
        assertNotNull(appDatabase, "AppDatabase instance should not be null")
    }

    @Test
    fun `locationDao instance is not null`() {
        assertNotNull(locationDao, "LocationDao instance should not be null")
    }

    @Test
    fun `weatherDao instance is not null`() {
        assertNotNull(weatherDao, "WeatherDao instance should not be null")
    }

    @Test
    fun `getInstance returns the same instance on subsequent calls`() {
        val instance1 = AppDatabase.getInstance(mock())
        val instance2 = AppDatabase.getInstance(mock())

        assertEquals(instance1, instance2, "getInstance should return the same instance")
    }

    @Test
    fun `getInstance initializes the database correctly`() {
        val instance = AppDatabase.getInstance(mock())
        assertNotNull(instance, "getInstance should initialize and return a non-null instance")
    }
}
