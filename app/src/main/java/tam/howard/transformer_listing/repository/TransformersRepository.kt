package tam.howard.transformer_listing.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.ResultFailure
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformerEdit
import tam.howard.transformer_listing.provider.api.TransformersApiProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransformersRepository @Inject constructor(private val apiProvider: TransformersApiProvider) {

    val onTransformersChanged: MutableSharedFlow<Unit> = MutableSharedFlow()

    suspend fun getTransformers(): Result<List<Transformer>> {
        return when (val result = apiProvider.getTransformers()) {
            is Result.Success -> result.map { it.transformers }
            is Result.Failure -> result
        }
    }

    suspend fun createTransformer(transformerEdit: TransformerEdit): Result<Transformer> {
        if (!transformerEdit.isDataValid) {
            return Result.Failure(failure = ResultFailure.InputInvalid)
        }

        return when (val result = apiProvider.createTransformer(transformerEdit)) {
            is Result.Success -> {
                onTransformersChanged.emit(Unit)
                result
            }
            is Result.Failure -> result
        }
    }

    suspend fun updateTransformer(transformerEdit: TransformerEdit): Result<Transformer> {
        if (transformerEdit.id.isNullOrBlank() || !transformerEdit.isDataValid) {
            return Result.Failure(failure = ResultFailure.InputInvalid)
        }

        return when (val result = apiProvider.updateTransformer(transformerEdit)) {
            is Result.Success -> {
                onTransformersChanged.emit(Unit)
                result
            }
            is Result.Failure -> result
        }
    }
}