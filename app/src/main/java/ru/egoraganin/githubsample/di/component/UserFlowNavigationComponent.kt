package ru.egoraganin.githubsample.di.component

import dagger.Subcomponent
import ru.egoraganin.githubsample.di.module.FlowNavigationModule
import ru.egoraganin.githubsample.di.scope.FlowScope
import ru.egoraganin.githubsample.ui.repos.ReposFragment
import ru.egoraganin.githubsample.ui.repos.UserFlowFragment

@FlowScope
@Subcomponent(modules = [FlowNavigationModule::class])
interface UserFlowNavigationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun flowNavigationModule(module: FlowNavigationModule): Builder
        fun build(): UserFlowNavigationComponent
    }

    fun inject(fragment: ReposFragment)
    fun inject(fragment: UserFlowFragment)
}
