package ru.egoraganin.githubsample.ui.auth

import android.os.Bundle
import org.androidannotations.annotations.EFragment
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.di.InjectionHolder
import ru.egoraganin.githubsample.di.qualifier.FlowType
import ru.egoraganin.githubsample.model.system.customtabs.ICustomTabManager
import ru.egoraganin.githubsample.ui.common.FlowFragment
import javax.inject.Inject

@EFragment(R.layout.layout_container)
open class AuthFlowFragment : FlowFragment() {

    @Inject
    internal lateinit var customTabManager: ICustomTabManager

    override fun getLaunchScreen() = Screens.ChoiceLoginMethod

    override val flowType = FlowType.ANONYMOUS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectionHolder.anonymousFlowNavigationComponent?.inject(this)

        context?.let { customTabManager.bindCustomTabService(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let { customTabManager.unbindCustomTabService(it) }
    }

    companion object {
        fun newInstance(): AuthFlowFragment {
            return AuthFlowFragment_()
        }
    }
}