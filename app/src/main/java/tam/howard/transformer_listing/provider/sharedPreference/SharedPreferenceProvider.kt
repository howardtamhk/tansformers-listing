package tam.howard.transformer_listing.provider.sharedPreference

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceProvider @Inject constructor(private val sharedPreferences: SharedPreferences) {

    var allSpark: String?
        get() {
            return sharedPreferences.getString(ALL_SPARK_KEY, null)
        }
        set(value) {
            value?.let {
                sharedPreferences.edit {
                    putString(ALL_SPARK_KEY, it)
                    apply()
                }
            } ?: run {
                sharedPreferences.edit {
                    remove(ALL_SPARK_KEY)
                }
            }
        }

    companion object {
        const val ALL_SPARK_KEY: String = "ALL_SPARK_KEY"
    }
}