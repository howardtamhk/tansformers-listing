package tam.howard.transformer_listing.core

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<out B : ViewDataBinding, in T>(protected val binding: B) :
    RecyclerView.ViewHolder(binding.root) {

    open fun onBind(model: T) {}
}
