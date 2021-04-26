package tam.howard.transformer_listing.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure
import tam.howard.transformer_listing.provider.api.TransformersApiProvider
import tam.howard.transformer_listing.provider.sharedPreference.SharedPreferenceProvider

class AllSparkRepositoryTest {

    lateinit var allSparkHolder: AllSparkHolder
    lateinit var apiProviderMock: TransformersApiProvider
    lateinit var sharedPreferenceProviderMock: SharedPreferenceProvider
    lateinit var repository: AllSparkRepository

    @Before
    fun setup() {
        allSparkHolder = AllSparkHolder()
        apiProviderMock = mockk()
        sharedPreferenceProviderMock = mockk(relaxed = true)

        repository = AllSparkRepository(
            allSparkHolder = allSparkHolder,
            transformersApiProvider = apiProviderMock,
            sharedPreferenceProvider = sharedPreferenceProviderMock,
        )
    }

    @Test
    fun `sharedPreference returning valid AllSpark returns AllSpark`() {
        val validAllSpark = "valid AllSpark"

        every { sharedPreferenceProviderMock.allSpark } returns validAllSpark
        runBlockingTest {
            val result = repository.initAllSpark()
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).value).isEqualTo(validAllSpark)
            assertThat(allSparkHolder.allSpark).isEqualTo(validAllSpark)
        }

    }

    @Test
    fun `sharedPreference returning empty AllSpark returns api call valid AllSpark`() {
        val emptyAllSpark = ""
        val apiCallValidAllSpark = "valid AllSpark"

        every { sharedPreferenceProviderMock.allSpark } returns emptyAllSpark
        coEvery { apiProviderMock.getAllSpark() } returns Result.Success(200, apiCallValidAllSpark)

        runBlockingTest {

            val result = repository.initAllSpark()
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).value).isEqualTo(apiCallValidAllSpark)
            assertThat(allSparkHolder.allSpark).isEqualTo(apiCallValidAllSpark)
        }
    }

    @Test
    fun `sharedPreference returning null AllSpark returns api call valid AllSpark`() {
        val apiCallValidAllSpark = "valid AllSpark"

        every { sharedPreferenceProviderMock.allSpark } returns null
        coEvery { apiProviderMock.getAllSpark() } returns Result.Success(200, apiCallValidAllSpark)

        runBlockingTest {

            val result = repository.initAllSpark()
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).value).isEqualTo(apiCallValidAllSpark)
            assertThat(allSparkHolder.allSpark).isEqualTo(apiCallValidAllSpark)
        }
    }

    @Test
    fun `api call returning Failure AllSpark returns Failure `() {
        val apiCallFailure = Result.Failure(500, ResultFailure.NetworkFailure(RuntimeException()))

        every { sharedPreferenceProviderMock.allSpark } returns null
        coEvery { apiProviderMock.getAllSpark() } returns apiCallFailure

        runBlockingTest {

            val result = repository.initAllSpark()
            assertThat(result).isInstanceOf(Result.Failure::class.java)
            assertThat(result).isEqualTo(apiCallFailure)
            assertThat(allSparkHolder.allSpark).isNull()
        }
    }

    @Test
    fun `api call returning empty AllSpark returns Failure `() {
        val apiCallSuccess = Result.Success(200, "")

        every { sharedPreferenceProviderMock.allSpark } returns null
        coEvery { apiProviderMock.getAllSpark() } returns apiCallSuccess

        runBlockingTest {

            val result = repository.initAllSpark()
            assertThat(result).isInstanceOf(Result.Failure::class.java)
            assertThat(allSparkHolder.allSpark).isNull()
        }
    }


}