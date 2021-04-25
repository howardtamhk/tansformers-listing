package tam.howard.transformer_listing.model.transformers

import kotlinx.serialization.Serializable

@Serializable
data class TransformersResponse(
    val transformers: List<Transformer> = listOf()
)
