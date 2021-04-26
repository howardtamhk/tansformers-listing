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
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformerEdit
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

    //region getTransformers
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

    @Test
    fun `createTransformer data valid and api returning Success returns Success`() {
        val editTransformer: TransformerEdit = mockk()
        val returningTransformer: Transformer = mockk()
        val result = Result.Success(
            200,
            returningTransformer
        )

        every { editTransformer.isDataValid } returns true
        coEvery { apiProvider.createTransformer(editTransformer) } returns result

        runBlockingTest {
            val actualResult = transformersRepository.createTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Success::class.java)
            assertThat((actualResult as Result.Success).value).isEqualTo(returningTransformer)
        }
    }

    @Test
    fun `createTransformer data valid and api returning Failure returns Failure`() {
        val editTransformer: TransformerEdit = mockk()
        val result = Result.Failure(
            500,
            ResultFailure.NetworkFailure(java.lang.RuntimeException())
        )

        every { editTransformer.isDataValid } returns true
        coEvery { apiProvider.createTransformer(editTransformer) } returns result

        runBlockingTest {
            val actualResult = transformersRepository.createTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat(actualResult).isEqualTo(result)
        }
    }

    @Test
    fun `createTransformer data invalid returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.isDataValid } returns false

        runBlockingTest {
            val actualResult = transformersRepository.createTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }
    //endregion getTransformers

    //region updateTransformers
    @Test
    fun `updateTransformer id, data valid and api returning Success returns Success`() {
        val editTransformer: TransformerEdit = mockk()
        val returningTransformer: Transformer = mockk()
        val result = Result.Success(
            200,
            returningTransformer
        )

        every { editTransformer.id } returns "id"
        every { editTransformer.isDataValid } returns true
        coEvery { apiProvider.updateTransformer(editTransformer) } returns result

        runBlockingTest {
            val actualResult = transformersRepository.updateTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Success::class.java)
            assertThat((actualResult as Result.Success).value).isEqualTo(returningTransformer)
        }
    }

    @Test
    fun `updateTransformer id, data valid and api returning Failure returns Failure`() {
        val editTransformer: TransformerEdit = mockk()
        val result = Result.Failure(
            500,
            ResultFailure.NetworkFailure(java.lang.RuntimeException())
        )

        every { editTransformer.id } returns "id"
        every { editTransformer.isDataValid } returns true
        coEvery { apiProvider.updateTransformer(editTransformer) } returns result

        runBlockingTest {
            val actualResult = transformersRepository.updateTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat(actualResult).isEqualTo(result)
        }
    }

    @Test
    fun `updateTransformer id null,  data valid returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.id } returns null
        every { editTransformer.isDataValid } returns false

        runBlockingTest {
            val actualResult = transformersRepository.updateTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }

    @Test
    fun `updateTransformer id empty string,  data valid returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.id } returns " "
        every { editTransformer.isDataValid } returns false

        runBlockingTest {
            val actualResult = transformersRepository.updateTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }

    @Test
    fun `updateTransformer id, data invalid returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.id } returns " "
        every { editTransformer.isDataValid } returns false

        runBlockingTest {
            val actualResult = transformersRepository.updateTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }
    //endregion updateTransformers

    //region delete
    @Test
    fun `deleteTransformer id valid and api returning Success returns Success`() {
        val editTransformer: TransformerEdit = mockk()
        val result = Result.Success(
            200,
            Unit
        )

        every { editTransformer.id } returns "id"
        coEvery { apiProvider.deleteTransformer("id") } returns result

        runBlockingTest {
            val actualResult = transformersRepository.deleteTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Success::class.java)
            assertThat((actualResult as Result.Success).value).isTrue()
        }
    }

    @Test
    fun `deleteTransformer id valid and api returning Failure with 204 returns Success`() {
        val editTransformer: TransformerEdit = mockk()
        val result = Result.Failure(
            204,
            ResultFailure.EmptyBody
        )

        every { editTransformer.id } returns "id"
        coEvery { apiProvider.deleteTransformer("id") } returns result

        runBlockingTest {
            val actualResult = transformersRepository.deleteTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Success::class.java)
            assertThat((actualResult as Result.Success).value).isTrue()
        }
    }

    @Test
    fun `deleteTransformer id valid and api returning Failure returns Failure`() {
        val editTransformer: TransformerEdit = mockk()
        val result = Result.Failure(
            500,
            ResultFailure.NetworkFailure(java.lang.RuntimeException())
        )

        every { editTransformer.id } returns "id"
        coEvery { apiProvider.deleteTransformer("id") } returns result

        runBlockingTest {
            val actualResult = transformersRepository.deleteTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat(actualResult).isEqualTo(result)
        }
    }

    @Test
    fun `deleteTransformer id null returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.id } returns null

        runBlockingTest {
            val actualResult = transformersRepository.deleteTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }

    @Test
    fun `deleteTransformer id empty string returns InputInvalid Failure`() {
        val editTransformer: TransformerEdit = mockk()

        every { editTransformer.id } returns " "

        runBlockingTest {
            val actualResult = transformersRepository.deleteTransformer(editTransformer)
            assertThat(actualResult).isInstanceOf(Result.Failure::class.java)
            assertThat((actualResult as Result.Failure).failure).isEqualTo(ResultFailure.InputInvalid)
        }
    }
    //endregion delete
}