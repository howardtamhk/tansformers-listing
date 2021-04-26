package tam.howard.transformer_listing.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivitySplashBinding
import tam.howard.transformer_listing.ui.listing.ListingActivity

@AndroidEntryPoint
class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {
    override val vm: SplashViewModel by viewModels()

    override fun bindUI() {
        super.bindUI()
        vm.allSparkReady.observe(this) {
            if (it) {
                startActivity(Intent(this, ListingActivity::class.java))
                finish()
            } else {
                Snackbar.make(binding.root, R.string.all_spark_unavailable, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
}