package tam.howard.transformer_listing.ui.listing

import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivityListingBinding
import tam.howard.transformer_listing.ui.edit.TransformerEditActivity
import tam.howard.transformer_listing.ui.edit.model.TransformerEditMode
import tam.howard.transformer_listing.ui.listing.model.TransformerFightResult
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

        transformerListAdapter.onItemClicked = {
            TransformerEditActivity.launch(this, TransformerEditMode.Edit, it)
        }

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

        vm.onFightResult.onEach {

            when (it.winningTeam) {
                TransformerFightResult.WinningTeam.Tie,
                TransformerFightResult.WinningTeam.Autobot,
                TransformerFightResult.WinningTeam.Deception,
                TransformerFightResult.WinningTeam.AutoWinCompetition -> {
                    showFightResultDialog(it)
                }
                null -> {
                    Snackbar.make(binding.root, R.string.no_fights, Snackbar.LENGTH_SHORT)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showFightResultDialog(transformerFightResult: TransformerFightResult) {
        val msg = when (transformerFightResult.winningTeam) {
            TransformerFightResult.WinningTeam.Tie -> {
                getString(R.string.fight_result_tie, transformerFightResult.noOfFight)
            }
            TransformerFightResult.WinningTeam.Autobot,
            TransformerFightResult.WinningTeam.Deception -> {
                getString(
                    R.string.fight_result_msg,
                    transformerFightResult.noOfFight,
                    transformerFightResult.winningTeam.toString(),
                    transformerFightResult.winningTeamLastTransformer?.name ?: "",
                    if (transformerFightResult.winningTeam == TransformerFightResult.WinningTeam.Autobot) {
                        TransformerFightResult.WinningTeam.Autobot.toString()
                    } else {
                        TransformerFightResult.WinningTeam.Autobot
                    },
                    if (transformerFightResult.survivorFromLosingTeam.isEmpty()) {
                        getString(R.string.no_survivors)
                    } else {

                        transformerFightResult.survivorFromLosingTeam.map { it.name }.joinToString()
                    }
                )
            }
            TransformerFightResult.WinningTeam.AutoWinCompetition -> {
                getString(R.string.all_transformer_destroyed)
            }
            null -> null
        } ?: return

        MaterialAlertDialogBuilder(this).setTitle(R.string.fight_result)
            .setMessage(msg)
            .setNeutralButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

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