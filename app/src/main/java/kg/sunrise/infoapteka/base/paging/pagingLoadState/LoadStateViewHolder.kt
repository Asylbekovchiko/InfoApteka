package kg.sunrise.infoapteka.base.paging.pagingLoadState

import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.paging.LoadState
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH

class LoadStateViewHolder(
    parent: ViewGroup,
    @LayoutRes id: Int,
    retry: () -> Unit
) : BaseVH<LoadState>(parent, id) {

    private val retryBtn = itemView.findViewById<TextView>(R.id.btn_retry)
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.load_progress_bar)

    init {
        retryBtn.setOnClickListener { retry.invoke() }
    }

    override fun bind(item: LoadState) {
        progressBar.isVisible = item is LoadState.Loading
        retryBtn.isVisible = item is LoadState.Error
    }
}