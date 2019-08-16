package ru.egoraganin.githubsample.ui.common

import android.content.Intent
import android.os.Bundle
import org.androidannotations.annotations.EFragment
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.Screens
import ru.egoraganin.githubsample.di.AppGraphManager
import ru.egoraganin.githubsample.di.IAppGraphManager
import ru.egoraganin.githubsample.di.qualifier.FlowType
import ru.egoraganin.githubsample.di.qualifier.NavigationQualifiers
import ru.egoraganin.githubsample.extention.newRootScreen
import ru.egoraganin.githubsample.model.system.provider.IDispatcherProvider
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject
import javax.inject.Named

@EFragment(R.layout.layout_container)
abstract class FlowFragment: BaseFragment() {

    @Inject @field:Named(NavigationQualifiers.FLOW_NAVIGATION)
    internal lateinit var navigatorHolder: NavigatorHolder

    @Inject
    override lateinit var dispatcherProvider: IDispatcherProvider

    @Inject
    internal lateinit var router: Router

    @Inject
    internal lateinit var graphManager: IAppGraphManager

    private var isInstanceStateSaved: Boolean = false

    private val navigator: Navigator by lazy(LazyThreadSafetyMode.NONE) {
        object : SupportAppNavigator(this.activity, childFragmentManager, R.id.layout_container) {
            override fun activityBack() {
                router.exit()
            }

            override fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
                val screen = when (command) {
                    is Forward -> command.screen
                    is Replace -> command.screen
                    else -> null
                }

                return when (screen) {
                    is Screens.CustomTabsScreen -> screen.customTabsIntent.startAnimationBundle
                    else -> super.createStartActivityOptions(command, activityIntent)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isInstanceStateSaved = true
    }

    override fun onStart() {
        super.onStart()
        isInstanceStateSaved = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            AppGraphManager.instance().initFlowNavigationGraph(flowType)
        }

        if (childFragmentManager.fragments.isEmpty()) {
            navigator.newRootScreen(getLaunchScreen())
        }
    }

    override fun onResume() {
        super.onResume()
        isInstanceStateSaved = false
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRealDestroy()) {
            graphManager.deinitFlowNavigationGraph(flowType)
        }
    }

    private fun isRealDestroy(): Boolean {
        if (activity?.isFinishing == true) {
            return true
        }

        if (isInstanceStateSaved) {
            isInstanceStateSaved = false
            return false
        }

        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            return true
        }

        return false
    }

    abstract val flowType: FlowType
    abstract fun getLaunchScreen(): SupportAppScreen
}