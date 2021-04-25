package tam.howard.transformer_listing.ui.listing

import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivityListingBinding
import tam.howard.transformer_listing.ui.edit.TransformerEditActivity
import tam.howard.transformer_listing.ui.edit.model.TransformerEditMode
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
        initToolbar(binding.toolbarListing, false)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listing_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_listing_add -> {
                TransformerEditActivity.launch(this, TransformerEditMode.Create)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}