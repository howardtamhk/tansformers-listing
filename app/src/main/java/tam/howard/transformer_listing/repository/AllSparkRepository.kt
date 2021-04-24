package tam.howard.transformer_listing.repository

import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure
import tam.howard.transformer_listing.provider.api.TransformersApiProvider
import tam.howard.transformer_listing.provider.sharedPreference.SharedPreferenceProvider
import tam.howard.transformer_listing.utils.extension.isNotNullOrBlank
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllSparkRepository @Inject constructor(
    private val allSparkHolder: AllSparkHolder,
    private val transformersApiProvider: TransformersApiProvider,
    private val sharedPreferenceProvider: SharedPreferenceProvider,
) {

    suspend fun initAllSpark(): Result<String> {
        sharedPreferenceProvider.allSpark?.let {
            if (it.isNotNullOrBlank()) {
                allSparkHolder.allSpark = it
                return Result.Success(value = it)
            }
        }

        return when (val result = transformersApiProvider.getAllSpark()) {
            is Result.Success -> {
                return if (result.value.isNotBlank()) {
                    allSparkHolder.allSpark = result.value
                    result
                } else {
                    Result.Failure(failure = ResultFailure.EmptyBody)
                }

            }
            is Result.Failure -> result
        }
    }
}

/**
 * pulled up level to contain AppSpark token. To avoid cycle dependency between okhttp interceptor and AppSparkRepository
 */
@Singleton
class AllSparkHolder @Inject constructor() {
    var allSpark: String? = null
}