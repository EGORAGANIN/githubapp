package ru.egoraganin.githubsample.di

import ru.egoraganin.githubsample.di.component.AnonymousFlowNavigationComponent
import ru.egoraganin.githubsample.di.component.BootstrapComponent
import ru.egoraganin.githubsample.di.component.UserComponent
import ru.egoraganin.githubsample.di.component.UserFlowNavigationComponent
import kotlin.properties.Delegates.notNull

object InjectionHolder {

    var bootstrapComponent: BootstrapComponent by notNull()

    var anonymousFlowNavigationComponent: AnonymousFlowNavigationComponent? = null

    var userFlowNavigationComponent: UserFlowNavigationComponent? = null

    var userComponent: UserComponent? = null
}