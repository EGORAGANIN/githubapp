package ru.egoraganin.githubsample.model.data.exception

interface IExceptionHandler {
    fun handleException(exception: Throwable, messageCallback: (messageStringRes: Int) -> Unit)
}