package tam.howard.transformer_listing.ui.listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
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
import tam.howard.transformer_listing.ui.listing.model.TransformerFightResult

@RunWith(MockitoJUnitRunner::class)
class ListingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var transformersRepository: TransformersRepository
    lateinit var vm: ListingViewModel
    lateinit var transformerListLiveDataObserver: Observer<List<Transformer>>
    lateinit var uiStateLiveDataObserver: Observer<ListingUIState>
    lateinit var helper: TransformerFightHelper

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        transformersRepository = mockk(relaxed = true)
        transformerListLiveDataObserver = mockk(relaxed = true)
        uiStateLiveDataObserver = mockk(relaxed = true)
        helper = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //region reload
    @Test
    fun `reload repository return non-empty list ui state is Result`() {
        val transformers = listOf<Transformer>(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
        )
        coEvery { transformersRepository.getTransformers() } returns Result.Success(value = transformers)

        vm = ListingViewModel(transformersRepository, helper)
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

        vm = ListingViewModel(transformersRepository, helper)
        vm.transformerList.observeForever(transformerListLiveDataObserver)
        vm.uiState.observeForever(uiStateLiveDataObserver)
        vm.reload()
        assertThat(vm.transformerList.value).isEmpty()
        assertThat(vm.uiState.value).isEqualTo(ListingUIState.Empty)
    }

    @Test
    fun `reload repository return Failure ui state is Error`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Failure(failure = ResultFailure.EmptyBody)

        vm = ListingViewModel(transformersRepository, helper)
        vm.transformerList.observeForever(transformerListLiveDataObserver)
        vm.uiState.observeForever(uiStateLiveDataObserver)
        vm.reload()
        assertThat(vm.uiState.value).isEqualTo(ListingUIState.Error)
    }
    //endregion reload

    //region fight
    @Test
    fun `empty transformer list onFightResult 0 noOfFight`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Failure(failure = ResultFailure.EmptyBody)

        vm = ListingViewModel(transformersRepository, helper)
        val vmSpy = spyk(vm) {
            every { transformerList.value } returns null
        }

        runBlockingTest {
            val job = launch {
                vmSpy.onFightResult.collect {
                    assertThat(it.noOfFight).isEqualTo(0)
                }
            }

            vmSpy.fight()
            job.cancel()
        }

    }

    @Test
    fun `fight helper return Autobot wins onFightResult Autobot wins`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Failure(failure = ResultFailure.EmptyBody)

        vm = ListingViewModel(transformersRepository, helper)
        val vmSpy = spyk(vm) {
            every { transformerList.value } returns listOf(mockk())
        }

        val result: TransformerFightResult = mockk {
            every { winningTeam } returns TransformerFightResult.WinningTeam.Autobot
        }
        every { helper.fight(any()) } returns result

        runBlockingTest {
            val job = launch {
                vmSpy.onFightResult.collect {
                    assertThat(it).isEqualTo(result)
                }
            }

            vmSpy.fight()
            job.cancel()
        }

    }

    @Test
    fun `fight helper return Deception wins onFightResult Deception wins`() {
        coEvery { transformersRepository.getTransformers() } returns Result.Failure(failure = ResultFailure.EmptyBody)

        vm = ListingViewModel(transformersRepository, helper)
        val vmSpy = spyk(vm) {
            every { transformerList.value } returns listOf(mockk())
        }

        val result: TransformerFightResult = mockk {
            every { winningTeam } returns TransformerFightResult.WinningTeam.Deception
        }
        every { helper.fight(any()) } returns result

        runBlockingTest {
            val job = launch {
                vmSpy.onFightResult.collect {
                    assertThat(it).isEqualTo(result)
                }
            }

            vmSpy.fight()
            job.cancel()
        }

    }
    //endregion fight

}