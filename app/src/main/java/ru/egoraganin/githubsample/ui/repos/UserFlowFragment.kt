package ru.egoraganin.githubsample.ui.repos

import android.os.Bundle
import org.androidannotations.annotations.EFragment
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.di.InjectionHolder
import ru.egoraganin.githubsample.di.qualifier.FlowType
import ru.egoraganin.githubsample.ui.common.FlowFragment

@EFragment(R.layout.layout_container)
open class UserFlowFragment: FlowFragment() {

    override fun getLaunchScreen() =  Screens.UserReposList

    override val flowType: FlowType = FlowType.USER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectionHolder.userFlowNavigationComponent?.inject(this)
    }

    companion object {
        fun newInstance(): UserFlowFragment {
            return UserFlowFragment_()
        }
    }
}