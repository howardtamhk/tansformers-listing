package tam.howard.transformer_listing.provider.api

import retrofit2.http.GET
import tam.howard.transformer_listing.model.Result
import tam.howard.transformer_listing.model.transformers.TransformersResponse

interface TransformersApiProvider {

    @GET("/allspark")
    suspend fun getAllSpark(): Result<String>

    @GET("/transformers")
    suspend fun getTransformers(): Result<TransformersResponse>
}