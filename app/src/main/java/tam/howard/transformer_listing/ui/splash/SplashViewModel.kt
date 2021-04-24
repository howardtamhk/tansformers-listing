package tam.howard.transformer_listing.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tam.howard.transformer_listing.core.BaseViewModel
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.repository.AllSparkRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val allSparkRepository: AllSparkRepository) :
    BaseViewModel() {

    val allSparkReady: LiveData<Boolean> = liveData {
        emit(allSparkRepository.initAllSpark() is Result.Success)
    }
}