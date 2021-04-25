package tam.howard.transformer_listing.ui.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import tam.howard.transformer_listing.core.BaseViewModel
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.repository.TransformersRepository
import tam.howard.transformer_listing.ui.listing.model.ListingUIState
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(private val transformersRepository: TransformersRepository) :
    BaseViewModel() {

    private val _uiState: MutableLiveData<ListingUIState> = MutableLiveData()
    val uiState: LiveData<ListingUIState> = _uiState
    val isRecyclerViewVisible: LiveData<Boolean> = uiState.map {
        it in listOf(ListingUIState.Loading, ListingUIState.Result)
    }

    private val _transformerList: MutableLiveData<List<Transformer>> = MutableLiveData()
    val transformerList: LiveData<List<Transformer>> = _transformerList

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
}