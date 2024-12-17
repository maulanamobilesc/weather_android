import com.maulana.weathermobile.data.datasource.local.AppDatabase
import com.maulana.weathermobile.data.datasource.local.LocationLocalDataSourceImpl
import com.maulana.weathermobile.data.datasource.local.dao.LocationDao
import com.maulana.weathermobile.data.datasource.local.entity.LocationEntity
import com.maulana.weathermobile.domain.model.LocationLocal
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LocationLocalDataSourceImplTest {

    // Mock dependencies
    private val mockDatabase: AppDatabase = mock(AppDatabase::class.java)
    private val mockLocationDao: LocationDao = mock(LocationDao::class.java)

    // Class under test
    private lateinit var dataSource: LocationLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        // Mock the database to return the mock DAO
        `when`(mockDatabase.locationDao()).thenReturn(mockLocationDao)

        dataSource = LocationLocalDataSourceImpl(mockDatabase)
    }

    @Test
    fun `getAllSavedLocation should return mapped list of LocationLocal`() = runTest {
        // Given: fake data from the database
        val fakeEntityList = listOf(
            LocationEntity(1, "Jakarta", -6.2, 106.8, true),
            LocationEntity(2, "Berlin", 52.5, 13.4, false)
        )

        val expectedLocations = listOf(
            LocationLocal(1, "Jakarta", -6.2, 106.8, true, "", ""),
            LocationLocal(2, "Berlin", 52.5, 13.4, false, "", "")
        )

        // Mock DAO behavior
        `when`(mockLocationDao.getAllSavedLocation()).thenReturn(flowOf(fakeEntityList))

        // When
        val result = dataSource.getAllSavedLocation().toList()

        // Then
        assertEquals(listOf(expectedLocations), result)
        verify(mockLocationDao).getAllSavedLocation()
    }

    @Test
    fun `insertLocation should call insertLocation on DAO with mapped entity`() = runTest {
        // Given: A LocationLocal object to insert
        val locationLocal = LocationLocal(1, "Tokyo", 35.7, 139.7, false, "", "")
        val expectedEntity = LocationEntity(1, "Tokyo", 35.7, 139.7, false)

        // When
        dataSource.insertLocation(locationLocal)

        // Then: Verify the DAO method is called with the mapped entity
        verify(mockLocationDao).insertLocation(expectedEntity)
    }
}
