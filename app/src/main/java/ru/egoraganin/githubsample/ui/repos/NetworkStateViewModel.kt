package ru.egoraganin.githubsample.ui.repos

import androidx.annotation.StringRes

@Suppress("DataClassPrivateConstructor")
data class NetworkStateViewModel private constructor(
    val status: Status,
    @StringRes val messageId: Int? = null
) {

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {
        val LOADED = NetworkStateViewModel(Status.SUCCESS)
        val LOADING = NetworkStateViewModel(Status.RUNNING)
        fun error(@StringRes messageId: Int?) = NetworkStateViewModel(Status.FAILED, messageId)
    }
}
