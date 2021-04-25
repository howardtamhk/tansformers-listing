package tam.howard.transformer_listing.repository

import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.provider.api.TransformersApiProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransformersRepository @Inject constructor(private val apiProvider: TransformersApiProvider) {

    suspend fun getTransformers(): Result<List<Transformer>> {
        return when (val result = apiProvider.getTransformers()) {
            is Result.Success -> result.map { it.transformers }
            is Result.Failure -> result
        }
    }
}