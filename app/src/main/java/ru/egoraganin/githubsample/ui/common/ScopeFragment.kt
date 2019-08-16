package ru.egoraganin.githubsample.ui.common

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import moxy.MvpAppCompatFragment
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment : MvpAppCompatFragment(), CoroutineScope {

    protected open lateinit var dispatcherProvider: IDispatcherProvider

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + dispatcherProvider.main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = SupervisorJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}