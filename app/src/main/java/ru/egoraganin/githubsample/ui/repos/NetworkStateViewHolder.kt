package ru.egoraganin.githubsample.ui.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.extention.isVisibleGone

class NetworkStateViewHolder private constructor(itemView: View, private val retryCallback: () -> Unit): RecyclerView.ViewHolder(itemView) {

    private val loadingProgress: ProgressBar = itemView.findViewById(R.id.progress_loading)
    private val retryButton: Button = itemView.findViewById<Button>(R.id.button_retry).apply {
        setOnClickListener { retryCallback.invoke() }
    }
    private val errorMessageLabel: TextView = itemView.findViewById(R.id.label_error_message)
    private val loadingLabel: TextView = itemView.findViewById(R.id.label_loading)

    fun bindTo(networkState: NetworkStateViewModel) {
        val isLoading = networkState.status == NetworkStateViewModel.Status.RUNNING
        loadingProgress.isVisibleGone(isLoading)
        loadingLabel.isVisibleGone(isLoading)

        val isFailed = networkState.status == NetworkStateViewModel.Status.FAILED
        retryButton.isVisibleGone(isFailed)
        errorMessageLabel.isVisibleGone(networkState.messageId != null)
        val messageId = networkState.messageId
        if (messageId != null) errorMessageLabel.setText(messageId) else errorMessageLabel.text = null
    }

    companion object {
        fun newInstance(parent: ViewGroup, retryCallback: () -> Unit) : NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_network_state, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }
}
