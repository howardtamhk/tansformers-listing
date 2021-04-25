package tam.howard.transformer_listing.provider.api

import retrofit2.http.*
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformerEdit
import tam.howard.transformer_listing.model.transformers.TransformersResponse

interface TransformersApiProvider {

    @GET("/allspark")
    suspend fun getAllSpark(): Result<String>

    @GET("/transformers")
    suspend fun getTransformers(): Result<TransformersResponse>

    @POST("/transformers")
    suspend fun createTransformer(@Body transformerEdit: TransformerEdit): Result<Transformer>

    @PUT("/transformers")
    suspend fun updateTransformer(@Body transformerEdit: TransformerEdit): Result<Transformer>
}