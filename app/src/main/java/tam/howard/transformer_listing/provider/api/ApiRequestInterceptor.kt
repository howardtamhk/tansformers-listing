package tam.howard.transformer_listing.provider.api

import okhttp3.Interceptor
import okhttp3.Response
import tam.howard.transformer_listing.repository.AllSparkHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRequestInterceptor @Inject constructor(private val allSparkHolder: AllSparkHolder) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")

        val allSpark = allSparkHolder.allSpark
        if (allSpark.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $allSpark")
        }

        return chain.proceed(requestBuilder.build())
    }

}