package ru.egoraganin.githubsample.model.system.provider

import androidx.arch.core.executor.ArchTaskExecutor
import java.util.concurrent.Executor

interface IExecutorProvider {
    val io: Executor
    val main: Executor
}

class ExecutorProvider : IExecutorProvider {
    override val io: Executor = ArchTaskExecutor.getIOThreadExecutor()
    override val main: Executor = ArchTaskExecutor.getMainThreadExecutor()
}
