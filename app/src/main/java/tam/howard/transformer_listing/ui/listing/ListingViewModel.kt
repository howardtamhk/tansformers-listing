package tam.howard.transformer_listing.ui.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tam.howard.transformer_listing.core.BaseViewModel
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.repository.TransformersRepository
import tam.howard.transformer_listing.ui.listing.model.ListingUIState
import tam.howard.transformer_listing.ui.listing.model.TransformerFightResult
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val transformersRepository: TransformersRepository,
    private val fightHelper: TransformerFightHelper
) :
    BaseViewModel() {

    private val _uiState: MutableLiveData<ListingUIState> = MutableLiveData()
    val uiState: LiveData<ListingUIState> = _uiState
    val isRecyclerViewVisible: LiveData<Boolean> = uiState.map {
        it in listOf(ListingUIState.Loading, ListingUIState.Result)
    }

    private val _transformerList: MutableLiveData<List<Transformer>> = MutableLiveData()
    val transformerList: LiveData<List<Transformer>> = _transformerList

    private val _onFightResult: MutableSharedFlow<TransformerFightResult> = MutableSharedFlow()
    val onFightResult: SharedFlow<TransformerFightResult> = _onFightResult

    init {
        reload()

        transformersRepository.onTransformersChanged.onEach {
            reload()
        }.launchIn(viewModelScope)
    }

    fun reload() {
        _uiState.value = ListingUIState.Loading
        viewModelScope.launch {
            when (val result = transformersRepository.getTransformers()) {
                is Result.Success -> {
                    _transformerList.value = result.value
                    _uiState.value = if (result.value.isEmpty()) {
                        ListingUIState.Empty
                    } else {
                        ListingUIState.Result
                    }
                }
                is Result.Failure -> {
                    _uiState.value = ListingUIState.Error
                }
            }
        }
    }

    fun fight() {
        viewModelScope.launch {
            val transformers: List<Transformer> = transformerList.value ?: kotlin.run {
                _onFightResult.emit(TransformerFightResult(0, null))
                return@launch
            }

            _onFightResult.emit(fightHelper.fight(transformers))
        }
    }


}