package ru.egoraganin.githubsample.model.repository.repo

sealed class NetworkStatus {
    object Running : NetworkStatus()
    object Success : NetworkStatus()
    data class Failed(val exception: Throwable) : NetworkStatus()
}