package ru.egoraganin.githubsample.ui.common

import android.content.Intent
import moxy.MvpAppCompatActivity
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import ru.egoraganin.githubsample.R

@EActivity
abstract class BaseActivity: MvpAppCompatActivity(), IDialogManagerProvider {

    lateinit var dialogFragmentManager: DialogFragmentManager

    protected val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.layout_container) as? BaseFragment

    @AfterViews
    protected open fun onInitInterface() {
        dialogFragmentManager = DialogFragmentManager(this)
    }

    override fun getDialogManager(): DialogFragmentManager {
        return dialogFragmentManager
    }

    override fun onNewIntent(intent: Intent?) {
        val fragment = currentFragment
        var result = false

        if (fragment is NewIntentReceiver && intent != null) {
            result = fragment.onNewIntent(intent)
        }

        if (!result) {
            super.onNewIntent(intent)
        }
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        var result = false

        if (fragment is BackPressed) {
            result = fragment.onBackPressed()
        }

        if (!result) {
            super.onBackPressed()
        }
    }

    interface BackPressed {
        fun onBackPressed(): Boolean
    }

    interface NewIntentReceiver {
        fun onNewIntent(intent: Intent): Boolean
    }
}