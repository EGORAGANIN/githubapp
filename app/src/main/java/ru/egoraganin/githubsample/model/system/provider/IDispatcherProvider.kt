package ru.egoraganin.githubsample.model.system.provider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface IDispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
}

class DispatcherProvider: IDispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val computation = Dispatchers.Default
}