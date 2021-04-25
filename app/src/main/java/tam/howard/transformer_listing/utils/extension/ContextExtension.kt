package tam.howard.transformer_listing.utils.extension

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)