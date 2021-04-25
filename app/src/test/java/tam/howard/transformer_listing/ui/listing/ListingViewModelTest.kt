package tam.howard.transformer_listing.ui.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.repository.TransformersRepository
import tam.howard.transformer_listing.ui.listing.model.ListingUIState

@RunWith(MockitoJUnitRunner::class)
class ListingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var transformersRepository: TransformersRepository
    lateinit var vm: ListingViewModel
    lateinit var transformerListLiveDataObserver: Observer<List<Transformer>>
    lateinit var uiStateLiveDataObserver: Observer<ListingUIState>

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        transformersRepository = mockk()
        transformerListLiveDataObserver = mockk(relaxed = true)
        uiStateLiveDataObserver = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `reload repository return non-empty list ui state is Result`() {
        val transformers = listOf<Transformer>(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
        )
        coEvery { transformersRepository.getTransformers() } returns Result.Success(value = transformers)

        vm = ListingViewModel(transformersRepository)
        vm.transformerList.observeForever(transformerListLiveDataObserver)
        vm.uiState.observeForever(uiStateLiveDataObserver)
        vm.reload()
        assertThat(vm.transformerList.value).hasSize(transformers.size)
        assertThat(vm.transformerList.value).containsExactlyElementsIn(transformers)
        assertThat(vm.uiState.value).isEqualTo(ListingUIState.Result)
    }

    @Test
    fun `reload repository return empty list ui state is Empty`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Success(value = listOf())

        vm = ListingViewModel(transformersRepository)
        vm.transformerList.observeForever(transformerListLiveDataObserver)
        vm.uiState.observeForever(uiStateLiveDataObserver)
        vm.reload()
        assertThat(vm.transformerList.value).isEmpty()
        assertThat(vm.uiState.value).isEqualTo(ListingUIState.Empty)
    }

    @Test
    fun `reload repository return Failure ui state is Error`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Failure(failure = ResultFailure.EmptyBody)

        vm = ListingViewModel(transformersRepository)
        vm.transformerList.observeForever(transformerListLiveDataObserver)
        vm.uiState.observeForever(uiStateLiveDataObserver)
        vm.reload()
        assertThat(vm.uiState.value).isEqualTo(ListingUIState.Error)
    }

}