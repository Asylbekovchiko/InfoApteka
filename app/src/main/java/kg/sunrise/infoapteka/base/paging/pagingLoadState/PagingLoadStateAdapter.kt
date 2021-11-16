package kg.sunrise.infoapteka.base.paging.pagingLoadState

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import kg.sunrise.infoapteka.R

class PagingLoadStateAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent, R.layout.rv_footer_load_state_item, retry)
    }
}