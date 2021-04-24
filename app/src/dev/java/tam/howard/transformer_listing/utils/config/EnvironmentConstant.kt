package tam.howard.transformer_listing.utils.config

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnvironmentConstant @Inject constructor() {
    val apiBaseUrl: String = "https://transformers-api.firebaseapp.com/"
}