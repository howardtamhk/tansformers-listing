package tam.howard.transformer_listing.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

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
    open fun bindUI() {}
}