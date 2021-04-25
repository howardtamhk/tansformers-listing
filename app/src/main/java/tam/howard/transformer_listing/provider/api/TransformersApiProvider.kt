package tam.howard.transformer_listing.provider.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("/transformers")
    suspend fun updateTransformer(@Body transformerEdit: TransformerEdit): Result<Transformer>
}