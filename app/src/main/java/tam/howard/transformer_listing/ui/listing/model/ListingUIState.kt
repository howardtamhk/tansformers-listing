package tam.howard.transformer_listing.ui.listing.model

import android.content.Context
import tam.howard.transformer_listing.R

enum class ListingUIState {
    Loading, Result, Empty, Error;

    fun uiMessage(context: Context): String {
        return when (this) {
            Empty -> context.getString(R.string.transformers_empty_list)
            Error -> context.getString(R.string.error_msg)
            else -> ""
        }
    }
}