package tam.howard.transformer_listing.provider.api

import retrofit2.http.GET
import tam.howard.transformer_listing.model.Result

interface TransformersApiProvider {

    @GET("/allspark")
    suspend fun getAllSpark(): Result<String>
}