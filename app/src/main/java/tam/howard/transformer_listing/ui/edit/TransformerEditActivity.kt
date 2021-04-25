package tam.howard.transformer_listing.ui.edit

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tam.howard.transformer_listing.R
import tam.howard.transformer_listing.core.BaseActivity
import tam.howard.transformer_listing.databinding.ActivityTransformerEditBinding
import tam.howard.transformer_listing.ui.edit.model.TransformerEditMode

@AndroidEntryPoint
class TransformerEditActivity :
    BaseActivity<ActivityTransformerEditBinding, TransformerEditViewModel>(R.layout.activity_transformer_edit) {
    override val vm: TransformerEditViewModel by viewModels()

    override fun initUI() {
        super.initUI()
        initToolbar(binding.toolbarTransformerEdit)
        binding.textFieldEditName.requestFocus()
        initSeekBarks()
    }

    override fun bindUI() {
        super.bindUI()
        binding.vm = vm

        vm.name.observe(this) {
            vm.editModel.value?.let { edit ->
                vm.editModel.value = edit.copy(name = it)
            }
        }

        vm.team.observe(this) {
            vm.editModel.value?.let { edit ->
                vm.editModel.value = edit.copy(team = it)
            }
        }

        vm.isSaveSuccess.onEach {
            if (it) {
                finish()
            } else {
                Snackbar.make(binding.root, R.string.error_msg, Snackbar.LENGTH_SHORT).show()
            }
        }.launchIn(lifecycleScope)
    }

    private fun initSeekBarks() {
        binding.seekbarEditStrength.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(strength = seekBar.progress)
                }
            }
        }

        binding.seekbarEditIntelligence.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(intelligence = seekBar.progress)
                }
            }
        }

        binding.seekbarEditSpeed.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(speed = seekBar.progress)
                }
            }
        }

        binding.seekbarEditEndurance.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(endurance = seekBar.progress)
                }
            }
        }

        binding.seekbarEditRank.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(rank = seekBar.progress)
                }
            }
        }

        binding.seekbarEditCourage.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(courage = seekBar.progress)
                }
            }
        }

        binding.seekbarEditFirepower.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(firepower = seekBar.progress)
                }
            }
        }

        binding.seekbarEditSkill.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {}
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
                if (seekBar == null) {
                    return
                }
                vm.editModel.value?.let {
                    vm.editModel.value = it.copy(skill = seekBar.progress)
                }
            }
        }
    }


    companion object {
        const val EDIT_MODE = "EDIT_MODE"

        fun launch(context: Context, mode: TransformerEditMode) {
            context.startActivity(
                Intent(context, TransformerEditActivity::class.java).putExtra(
                    EDIT_MODE,
                    mode.value
                )
            )
        }
    }
}