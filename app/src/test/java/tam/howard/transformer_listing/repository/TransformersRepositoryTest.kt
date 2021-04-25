package tam.howard.transformer_listing.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformersResponse
import tam.howard.transformer_listing.provider.api.TransformersApiProvider

class TransformersRepositoryTest {

    lateinit var apiProvider: TransformersApiProvider
    lateinit var transformersRepository: TransformersRepository

    @Before
    fun setUp() {
        apiProvider = mockk()
        transformersRepository = TransformersRepository(apiProvider)
    }

    @Test
    fun `getTransformers api returning Success returns Success`() {
        val transformerList = listOf<Transformer>(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
        )
        coEvery { apiProvider.getTransformers() } returns Result.Success(
            200,
            TransformersResponse(transformerList)
        )

        runBlockingTest {
            val result = transformersRepository.getTransformers()
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).value).hasSize(transformerList.size)
            assertThat(result.value).containsExactlyElementsIn(transformerList)
        }
    }

    @Test
    fun `getTransformers api returning Failure returns Failure`() {
        val result = Result.Failure(
            500,
            ResultFailure.NetworkFailure(RuntimeException())
        )
        coEvery { apiProvider.getTransformers() } returns result

        runBlockingTest {
            val result = transformersRepository.getTransformers()
            assertThat(result).isInstanceOf(Result.Failure::class.java)
            assertThat((result as Result.Failure)).isEqualTo(result)
        }
    }
}