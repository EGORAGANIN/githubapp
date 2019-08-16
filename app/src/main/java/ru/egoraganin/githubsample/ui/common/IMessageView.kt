package ru.egoraganin.githubsample.ui.common

import androidx.annotation.StringRes

interface IMessageView {
    fun showMessage(@StringRes messageResId: Int)
}