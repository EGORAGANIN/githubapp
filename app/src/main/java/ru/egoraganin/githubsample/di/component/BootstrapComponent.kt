package ru.egoraganin.githubsample.di.component

import dagger.Component
import ru.egoraganin.githubsample.di.module.AppNavigationModule
import ru.egoraganin.githubsample.di.module.BootstrapModule
import ru.egoraganin.githubsample.di.scope.BootScope
import ru.egoraganin.githubsample.ui.AppActivity

@BootScope
@Component(modules = [BootstrapModule::class, AppNavigationModule::class])
interface BootstrapComponent {

    fun flowNavigationBuilder(): AnonymousFlowNavigationComponent.Builder

    fun userGraphBuilder(): UserComponent.Builder

    fun inject(activity: AppActivity)
}