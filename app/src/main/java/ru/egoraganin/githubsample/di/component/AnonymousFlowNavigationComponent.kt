package ru.egoraganin.githubsample.di.component

import dagger.Subcomponent
import ru.egoraganin.githubsample.di.module.FlowNavigationModule
import ru.egoraganin.githubsample.di.scope.FlowScope
import ru.egoraganin.githubsample.ui.auth.AuthFlowFragment
import ru.egoraganin.githubsample.ui.auth.ChoiceLoginMethodFragment

@FlowScope
@Subcomponent(modules = [FlowNavigationModule::class])
interface AnonymousFlowNavigationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun flowNavigationModule(module: FlowNavigationModule): Builder
        fun build(): AnonymousFlowNavigationComponent
    }

    fun inject(fragment: AuthFlowFragment)
    fun inject(fragment: ChoiceLoginMethodFragment)
}
