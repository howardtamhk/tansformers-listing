package tam.howard.transformer_listing.ui.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
import tam.howard.transformer_listing.model.transformers.TransformerEdit
import tam.howard.transformer_listing.model.transformers.TransformerTeam
import tam.howard.transformer_listing.repository.TransformersRepository
import tam.howard.transformer_listing.ui.edit.model.TransformerEditMode
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(MockitoJUnitRunner::class)
class TransformerEditViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var transformersRepository: TransformersRepository
    lateinit var savedStateHandle: SavedStateHandle
    lateinit var vm: TransformerEditViewModel

    lateinit var isLoadingLiveDataObserver: Observer<Boolean>
    lateinit var nameLiveDataObserver: Observer<String>
    lateinit var teamLiveDataObserver: Observer<TransformerTeam>
    lateinit var isEditModelLiveDataObserver: Observer<Boolean>
    lateinit var transformerEditLiveDataObserver: Observer<TransformerEdit>

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        transformersRepository = mockk()
        savedStateHandle = mockk()

        isLoadingLiveDataObserver = mockk(relaxed = true)
        nameLiveDataObserver = mockk(relaxed = true)
        teamLiveDataObserver = mockk(relaxed = true)
        isEditModelLiveDataObserver = mockk(relaxed = true)
        transformerEditLiveDataObserver = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //region init
    @Test
    fun `init create mode setup`() {
        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Create.value
        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)

        vm.name.observeForever(nameLiveDataObserver)
        vm.team.observeForever(teamLiveDataObserver)
        vm.editModel.observeForever(transformerEditLiveDataObserver)

        assertThat(vm.name.value).isEqualTo("")
        assertThat(vm.team.value).isNull()

        assertThat(vm.editModel.value?.name).isEqualTo("")
        assertThat(vm.editModel.value?.strength).isEqualTo(5)
        assertThat(vm.editModel.value?.intelligence).isEqualTo(5)
        assertThat(vm.editModel.value?.speed).isEqualTo(5)
        assertThat(vm.editModel.value?.endurance).isEqualTo(5)
        assertThat(vm.editModel.value?.rank).isEqualTo(5)
        assertThat(vm.editModel.value?.courage).isEqualTo(5)
        assertThat(vm.editModel.value?.firepower).isEqualTo(5)
        assertThat(vm.editModel.value?.skill).isEqualTo(5)
        assertThat(vm.editModel.value?.team).isNull()
    }

    @Test
    fun `init edit mode setup`() {

        val editModel = TransformerEdit(
            id = "id",
            name = "name",
            strength = 4,
            intelligence = 2,
            speed = 8,
            endurance = 1,
            rank = 9,
            courage = 2,
            firepower = 4,
            skill = 6,
            team = TransformerTeam.Autobot
        )

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel
        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)

        vm.name.observeForever(nameLiveDataObserver)
        vm.team.observeForever(teamLiveDataObserver)
        vm.editModel.observeForever(transformerEditLiveDataObserver)

        assertThat(vm.name.value).isEqualTo(editModel.name)
        assertThat(vm.team.value).isEqualTo(editModel.team)

        assertThat(vm.editModel.value).isEqualTo(editModel)
    }
    //endregion init

    //region save create
    @Test
    fun `save create mode editModel valid Repository returning Success isActionSuccess true`() {
        val editModel: TransformerEdit = mockk()

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Create.value
        every { editModel.isDataValid } returns true
        coEvery { transformersRepository.createTransformer(editModel) } returns Result.Success(
            200,
            mockk()
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isTrue()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save create mode editModel valid Repository returning Failure isActionSuccess false`() {
        val editModel: TransformerEdit = mockk()

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Create.value
        every { editModel.isDataValid } returns true
        coEvery { transformersRepository.createTransformer(editModel) } returns Result.Failure(
            500,
            ResultFailure.ApiError(null)
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save create mode editModel invalid isActionSuccess false`() {
        val editModel: TransformerEdit = mockk()

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Create.value
        every { editModel.isDataValid } returns false

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }
    //endregion save create

    //region save edit
    @Test
    fun `save edit mode id,editModel valid Repository returning Success isActionSuccess true`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns true
        every { editModel.id } returns "id"

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel
        coEvery { transformersRepository.updateTransformer(editModel) } returns Result.Success(
            200,
            mockk()
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.isLoading.observeForever(isLoadingLiveDataObserver)

        vm.editModel.value = editModel

        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isTrue()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save edit mode id,editModel valid Repository returning Failure isActionSuccess false`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns true
        every { editModel.id } returns "id"

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel
        coEvery { transformersRepository.updateTransformer(editModel) } returns Result.Failure(
            500,
            ResultFailure.ApiError(null)
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save edit mode id null, editModel valid isActionSuccess false`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns true
        every { editModel.id } returns null

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save edit mode id empty string, editModel valid isActionSuccess false`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns true
        every { editModel.id } returns "  "

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save edit mode id valid, editModel invalid isActionSuccess false`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns false
        every { editModel.id } returns "id"

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }

    @Test
    fun `save edit mode id,editModel invalid isActionSuccess false`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.isDataValid } returns false
        every { editModel.id } returns null

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        vm.isEditModelValid.observeForever(isEditModelLiveDataObserver)
        vm.editModel.value = editModel

        vm.isLoading.observeForever(isLoadingLiveDataObserver)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }
    //endregion save edit

    //region delete create
    @Test
    fun `delete create mode isActionSuccess false`() {
        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Create.value
        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.save()
            job.cancel()
        }
    }
    //endregion delete create

    //region delete edit
    @Test
    fun `delete edit mode id valid Repository return Success isActionSuccess True`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.id } returns "id"

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel
        coEvery { transformersRepository.deleteTransformer(editModel) } returns Result.Success(
            200,
            mockk()
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isTrue()
                }
            }

            vm.deleteTransformer()
            job.cancel()
        }
    }

    @Test
    fun `delete edit mode id valid Repository return Failure isActionSuccess False`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.id } returns "id"

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel
        coEvery { transformersRepository.deleteTransformer(editModel) } returns Result.Failure(
            500,
            ResultFailure.ApiError(null)
        )

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.deleteTransformer()
            job.cancel()
        }
    }

    @Test
    fun `delete edit mode id null isActionSuccess False`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.id } returns null

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.deleteTransformer()
            job.cancel()
        }
    }

    @Test
    fun `delete edit mode id empty string isActionSuccess False`() {
        val editModel: TransformerEdit = mockk(relaxed = true)
        every { editModel.id } returns "   "

        every { savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE) } returns TransformerEditMode.Edit.value
        every { savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT) } returns editModel

        vm = TransformerEditViewModel(savedStateHandle, transformersRepository)
        runBlockingTest {
            val job = launch {
                vm.isActionSuccess.collect {
                    assertThat(it).isFalse()
                }
            }

            vm.deleteTransformer()
            job.cancel()
        }
    }
    //endregion delete edit
}