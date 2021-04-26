package tam.howard.transformer_listing.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
) : AppCompatActivity() {

    abstract val vm: VM
    protected lateinit var binding: B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, this.layoutId)
        this.binding.lifecycleOwner = this

        initUI()
        bindUI()
    }

    open fun initUI() {}
    open fun initToolbar(toolbar: MaterialToolbar, showUpButton: Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showUpButton)
        supportActionBar?.setDisplayShowHomeEnabled(showUpButton)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    open fun bindUI() {}
}