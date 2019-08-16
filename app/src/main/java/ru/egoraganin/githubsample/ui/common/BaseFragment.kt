package ru.egoraganin.githubsample.ui.common

import android.content.Intent
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EFragment
import ru.egoraganin.githubsample.R

@EFragment
abstract class BaseFragment: ScopedFragment(), BaseActivity.BackPressed, BaseActivity.NewIntentReceiver  {

    protected val currentFragment: BaseFragment?
        get() = childFragmentManager.findFragmentById(R.id.layout_container) as? BaseFragment

    @AfterViews
    protected open fun onInitInterface() {

    }

    protected fun getDialogManager(): DialogFragmentManager {
        val dialogManagerProvider = activity as IDialogManagerProvider
        return dialogManagerProvider.getDialogManager()
    }

    override fun onNewIntent(intent: Intent): Boolean {
        val fragment = currentFragment
        var result = false

        if (fragment is BaseActivity.NewIntentReceiver) {
            result = fragment.onNewIntent(intent)
        }

        return result
    }

    override fun onBackPressed(): Boolean {
        val fragment = currentFragment
        var result = false

        if (fragment is BaseActivity.BackPressed) {
            result = fragment.onBackPressed()
        }

        return result
    }
}