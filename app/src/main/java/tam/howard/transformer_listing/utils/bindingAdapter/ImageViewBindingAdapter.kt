package tam.howard.transformer_listing.utils.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("loadUrl")
fun loadImageUrl(view: ImageView, url: String?){
    if (url.isNullOrBlank()){
        return
    }

    view.load(url) {
        crossfade(true)
    }
}