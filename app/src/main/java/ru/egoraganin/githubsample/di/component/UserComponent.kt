package ru.egoraganin.githubsample.di.component

import dagger.Subcomponent
import ru.egoraganin.githubsample.di.module.UserModule
import ru.egoraganin.githubsample.di.scope.UserScope

@UserScope
@Subcomponent(modules = [UserModule::class])
interface UserComponent {

    @Subcomponent.Builder
    interface Builder {
        fun userModule(module: UserModule): Builder
        fun build(): UserComponent
    }

    fun flowNavigationBuilder(): UserFlowNavigationComponent.Builder
}