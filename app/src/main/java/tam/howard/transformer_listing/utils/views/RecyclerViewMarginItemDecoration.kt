package tam.howard.transformer_listing.utils.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped

class RecyclerViewMarginItemDecoration @AssistedInject constructor(
    @Assisted("marginValue") private val value: Int,
    @Assisted("orientation") private val orientation: Int
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                right = value
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                bottom = value
            }
        }
    }

    @AssistedFactory
    interface RecyclerViewMarginItemDecorationFactory {
        fun create(
            @Assisted("marginValue") value: Int,
            @Assisted("orientation") orientation: Int
        ): RecyclerViewMarginItemDecoration
    }
}
