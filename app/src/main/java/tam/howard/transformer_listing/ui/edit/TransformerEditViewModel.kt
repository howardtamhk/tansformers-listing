package tam.howard.transformer_listing.ui.edit

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import tam.howard.transformer_listing.core.BaseViewModel
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.TransformerEdit
import tam.howard.transformer_listing.model.transformers.TransformerTeam
import tam.howard.transformer_listing.repository.TransformersRepository
import tam.howard.transformer_listing.ui.edit.model.TransformerEditMode
import javax.inject.Inject

@HiltViewModel
class TransformerEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transformersRepository: TransformersRepository,
) :
    BaseViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val editMode: TransformerEditMode =
        savedStateHandle.get<String>(TransformerEditActivity.EDIT_MODE)
            ?.let { TransformerEditMode.customValueOf(it) }
            ?: TransformerEditMode.Create

    val name: MutableLiveData<String>
    val team: MutableLiveData<TransformerTeam>
    val editModel: MutableLiveData<TransformerEdit>

    val isEditModelValid: LiveData<Boolean>

    private val _isActionSuccess: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isActionSuccess: SharedFlow<Boolean> = _isActionSuccess

    init {
        when (editMode) {
            TransformerEditMode.Create -> {
                editModel = MutableLiveData(
                    TransformerEdit(
                        strength = 5,
                        intelligence = 5,
                        speed = 5,
                        endurance = 5,
                        rank = 5,
                        courage = 5,
                        firepower = 5,
                        skill = 5
                    )
                )
            }
            TransformerEditMode.Edit -> editModel = MutableLiveData(
                savedStateHandle.get<TransformerEdit>(TransformerEditActivity.TRANSFORMER_EDIT)
            )
        }

        isEditModelValid = editModel.map {
            when (editMode) {
                TransformerEditMode.Create -> it.isDataValid
                TransformerEditMode.Edit -> it.id != null && it.isDataValid
            }
        }
        name = MutableLiveData(editModel.value?.name)
        team = MutableLiveData(editModel.value?.team)
    }

    fun setTeam(teamValue: TransformerTeam) {
        team.value = teamValue
    }

    fun save() {
        viewModelScope.launch {
            if (isEditModelValid.value != true) {
                _isActionSuccess.emit(false)
                return@launch
            }
            val editModel = editModel.value ?: kotlin.run {
                _isActionSuccess.emit(false)
                return@launch
            }

            isLoading.value = true
            val result = when (editMode) {
                TransformerEditMode.Create -> transformersRepository.createTransformer(editModel)
                TransformerEditMode.Edit -> transformersRepository.updateTransformer(editModel)
            }

            _isActionSuccess.emit(result is Result.Success)
            isLoading.value = false
        }
    }

    fun deleteTransformer() {
        viewModelScope.launch {
            if (editMode == TransformerEditMode.Create) {
                _isActionSuccess.emit(false)
                return@launch
            }
            val editModel = editModel.value ?: kotlin.run {
                _isActionSuccess.emit(false)
                return@launch
            }

            isLoading.value = true
            val result = transformersRepository.deleteTransformer(editModel)
            _isActionSuccess.emit(result is Result.Success)
            isLoading.value = false
        }
    }


}