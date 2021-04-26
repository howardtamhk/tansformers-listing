package tam.howard.transformer_listing.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import tam.howard.transformer_listing.BuildConfig
import tam.howard.transformer_listing.provider.api.ApiRequestInterceptor
import tam.howard.transformer_listing.provider.api.ResultCallAdapterFactory
import tam.howard.transformer_listing.provider.api.TransformersApiProvider
import tam.howard.transformer_listing.utils.config.EnvironmentConstant
import tam.howard.transformer_listing.utils.network.TLS12SocketFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun providesOkHttpClient(
        requestInterceptor: ApiRequestInterceptor,
        tlS12SocketFactory: TLS12SocketFactory
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(requestInterceptor)
            if (BuildConfig.SHOW_LOG) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }

            try {
                tlS12SocketFactory.trustManager?.let {
                    sslSocketFactory(tlS12SocketFactory, it)
                }
            } catch (e: Throwable) {

            }

        }.build()
    }

    @Provides
    fun providesResultCallAdapterFactory(): ResultCallAdapterFactory {
        return ResultCallAdapterFactory()
    }

    @ExperimentalSerializationApi
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        resultCallAdapterFactory: ResultCallAdapterFactory,
        environmentConstant: EnvironmentConstant,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(environmentConstant.apiBaseUrl).client(okHttpClient)
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }.asConverterFactory(MediaType.get("application/json"))
            ).addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun providesTransformerApiProvider(retrofit: Retrofit): TransformersApiProvider {
        return retrofit.create(TransformersApiProvider::class.java)
    }
}