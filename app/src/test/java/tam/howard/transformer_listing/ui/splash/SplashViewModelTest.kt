package tam.howard.transformer_listing.ui.splash

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
import tam.howard.transformer_listing.repository.AllSparkRepository

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var allSparkRepository: AllSparkRepository
    lateinit var vm: SplashViewModel
    lateinit var liveDataObserver: Observer<Boolean>


    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        allSparkRepository = mockk()
        vm = SplashViewModel(allSparkRepository)
        liveDataObserver = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `repository returning Success returns true`() {
        coEvery { allSparkRepository.initAllSpark() } returns Result.Success(200, "valid AppSpark")
        vm.allSparkReady.observeForever(liveDataObserver)
        assertThat(vm.allSparkReady.value).isTrue()
    }

    @Test
    fun `repository returning Failure returns false`() {
        coEvery { allSparkRepository.initAllSpark() } returns Result.Failure(
            null,
            ResultFailure.EmptyBody
        )
        vm.allSparkReady.observeForever(liveDataObserver)
        assertThat(vm.allSparkReady.value).isFalse()
    }
}