package ru.egoraganin.githubsample.di

import android.annotation.SuppressLint
import android.content.Context
import ru.egoraganin.githubsample.di.component.DaggerBootstrapComponent
import ru.egoraganin.githubsample.di.module.AppNavigationModule
import ru.egoraganin.githubsample.di.module.BootstrapModule
import ru.egoraganin.githubsample.di.module.FlowNavigationModule
import ru.egoraganin.githubsample.di.module.UserModule
import ru.egoraganin.githubsample.di.qualifier.FlowType
import ru.egoraganin.githubsample.entity.login.UserIdentity

interface IAppGraphManager {
    fun initBootstrapGraph()
    fun initFlowNavigationGraph(flowType: FlowType)
    fun deinitFlowNavigationGraph(flowType: FlowType)
    fun initUserGraph(identity: UserIdentity)
    fun deinitUserGraph()
}

internal class AppGraphManager private constructor(
    private val context: Context
) : IAppGraphManager {

    override fun initBootstrapGraph() {

        val bootstrapComponent = DaggerBootstrapComponent.builder()
            .bootstrapModule(BootstrapModule(context))
            .appNavigationModule(AppNavigationModule())
            .build()

        InjectionHolder.bootstrapComponent = bootstrapComponent
    }

    override fun initFlowNavigationGraph(flowType: FlowType) {
        when (flowType) {
            FlowType.ANONYMOUS -> {
                InjectionHolder.anonymousFlowNavigationComponent = InjectionHolder.bootstrapComponent
                    .flowNavigationBuilder()
                    .flowNavigationModule(FlowNavigationModule())
                    .build()
            }
            FlowType.USER -> {
                val userComponent = InjectionHolder.userComponent ?: throw IllegalStateException("UserComponent not initialized")
                InjectionHolder.userFlowNavigationComponent = userComponent
                    .flowNavigationBuilder()
                    .flowNavigationModule(FlowNavigationModule())
                    .build()
            }
        }
    }

    override fun deinitFlowNavigationGraph(flowType: FlowType) {
        when (flowType) {
            FlowType.ANONYMOUS -> InjectionHolder.anonymousFlowNavigationComponent = null
            FlowType.USER -> InjectionHolder.userFlowNavigationComponent = null
        }
    }

    override fun initUserGraph(identity: UserIdentity) {
        val userComponent = InjectionHolder.bootstrapComponent
            .userGraphBuilder()
            .userModule(UserModule(identity))
            .build()

        InjectionHolder.userComponent = userComponent
    }

    override fun deinitUserGraph() {
        InjectionHolder.userComponent = null
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var INSTANCE: IAppGraphManager

        fun instance(): IAppGraphManager = INSTANCE

        fun initInstance(context: Context) {
            INSTANCE = AppGraphManager(context)
        }
    }
}