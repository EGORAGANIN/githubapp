package ru.egoraganin.githubsample.ui.repos

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.entity.repo.RepoModel

class ReposAdapter(
    diffUtilCallback: DiffUtil.ItemCallback<RepoModel>,
    private val retryCallback: () -> Unit
) : PagedListAdapter<RepoModel, RecyclerView.ViewHolder>(diffUtilCallback) {

    private var networkState: NetworkStateViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_repo_item -> RepoViewHolder.newInstance(parent)
            R.layout.layout_network_state -> NetworkStateViewHolder.newInstance(parent, retryCallback)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.layout_repo_item -> {
                // getItem == null when using placeholders
                getItem(position)?.let { (holder as RepoViewHolder).bind(it) }
            }
            R.layout.layout_network_state -> {
                networkState?.let { (holder as NetworkStateViewHolder).bindTo(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.layout_network_state
        } else {
            R.layout.layout_repo_item
        }
    }

    override fun getItemCount(): Int {
        var itemCount = super.getItemCount()

        if (hasExtraRow()) {
            itemCount += 1
        }

        return itemCount
    }

    fun setNetworkState(newNetworkState: NetworkStateViewModel) {
        val previousState = networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hasExtraRow) {
                notifyItemInserted(super.getItemCount())
            } else {
                notifyItemRemoved(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemRemoved(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkStateViewModel.LOADED
}
