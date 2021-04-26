package tam.howard.transformer_listing.utils.bindingAdapter

import androidx.databinding.BindingAdapter
import com.warkiz.widget.IndicatorSeekBar

@BindingAdapter("progress")
fun setProgressInt(seekBar: IndicatorSeekBar, progress: Int?) {
    seekBar.setProgress(progress?.toFloat() ?: seekBar.min)
}