package tam.howard.transformer_listing.ui.listing

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivityListingBinding

@AndroidEntryPoint
class ListingActivity :
    BaseActivity<ActivityListingBinding, ListingViewModel>(R.layout.activity_listing) {
    override val vm: ListingViewModel by viewModels()

}