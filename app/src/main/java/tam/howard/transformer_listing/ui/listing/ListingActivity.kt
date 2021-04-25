package tam.howard.transformer_listing.ui.listing

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivityListingBinding
import tam.howard.transformer_listing.utils.views.RecyclerViewMarginItemDecoration
import javax.inject.Inject

@AndroidEntryPoint
class ListingActivity :
    BaseActivity<ActivityListingBinding, ListingViewModel>(R.layout.activity_listing) {

    override val vm: ListingViewModel by viewModels()

    @Inject
    lateinit var transformerListAdapter: ListingAdapter

    @Inject
    lateinit var marginItemDecorationFactory: RecyclerViewMarginItemDecoration.RecyclerViewMarginItemDecorationFactory


    override fun initUI() {
        super.initUI()
        binding.recyclerViewListing.apply {
            val llm = LinearLayoutManager(this@ListingActivity, LinearLayoutManager.VERTICAL, false)
            layoutManager = llm
            adapter = transformerListAdapter
            addItemDecoration(
                marginItemDecorationFactory.create(
                    resources.getDimension(R.dimen.listing_item_margin_between).toInt(),
                    llm.orientation
                )
            )
        }
    }

    override fun bindUI() {
        super.bindUI()
        binding.vm = vm
        vm.transformerList.observe(this) {
            transformerListAdapter.submitList(it)
        }
    }
}