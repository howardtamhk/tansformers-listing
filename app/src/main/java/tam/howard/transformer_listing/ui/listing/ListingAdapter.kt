package tam.howard.transformer_listing.ui.listing

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.scopes.ActivityRetainedScoped
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseViewHolder
import tam.howard.transformer_listing.databinding.ViewHolderTransformerItemBinding
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.utils.extension.gone
import tam.howard.transformer_listing.utils.extension.layoutInflater
import tam.howard.transformer_listing.utils.extension.visible
import javax.inject.Inject

@ActivityRetainedScoped
class ListingAdapter @Inject constructor(private val imageLoader: ImageLoader) :
    ListAdapter<Transformer, ListingAdapter.TransformerItemViewHolder>(DIFF_UTILS) {

    var onItemClicked: ((Transformer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransformerItemViewHolder {
        return TransformerItemViewHolder(
            DataBindingUtil.inflate(
                parent.context.layoutInflater,
                R.layout.view_holder_transformer_item,
                parent,
                false
            ),
            imageLoader,
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: TransformerItemViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class TransformerItemViewHolder(
        binding: ViewHolderTransformerItemBinding,
        private val imageLoader: ImageLoader,
        private val onItemClicked: ((Transformer) -> Unit)?,
    ) :
        BaseViewHolder<ViewHolderTransformerItemBinding, Transformer>(binding) {
        override fun onBind(model: Transformer) {
            super.onBind(model)
            binding.item = model

            binding.imageViewTransformerItem.apply {
                if (model.teamIcon.isNotBlank()) {
                    imageLoader.enqueue(
                        ImageRequest.Builder(context)
                            .data(model.teamIcon)
                            .target(this)
                            .build()
                    )
                    visible()
                } else {
                    gone()
                }
            }


            binding.root.setOnClickListener {
                onItemClicked?.invoke(model)
            }
        }
    }

    companion object {
        val DIFF_UTILS = object : DiffUtil.ItemCallback<Transformer>() {
            override fun areItemsTheSame(oldItem: Transformer, newItem: Transformer): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Transformer, newItem: Transformer): Boolean =
                oldItem == newItem

        }
    }
}