package tam.howard.transformer_listing.di

import android.app.Application
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    @Provides
    fun providesImageLoader(application: Application, okHttpClient: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(application).okHttpClient(okHttpClient).build()
    }
}