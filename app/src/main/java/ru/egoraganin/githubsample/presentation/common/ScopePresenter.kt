package ru.egoraganin.githubsample.presentation.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import moxy.MvpPresenter
import moxy.MvpView
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import kotlin.coroutines.CoroutineContext

abstract class ScopePresenter<View: MvpView>(protected val dispatcherProvider: IDispatcherProvider): MvpPresenter<View>(), CoroutineScope {

    private var job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatcherProvider.main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}