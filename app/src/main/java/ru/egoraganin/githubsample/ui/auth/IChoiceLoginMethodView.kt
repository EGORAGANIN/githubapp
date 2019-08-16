package ru.egoraganin.githubsample.ui.auth

import moxy.MvpView
import ru.egoraganin.githubsample.ui.common.IMessageView

interface IChoiceLoginMethodView: IMessageView, MvpView {

    fun showProgress(isVisible: Boolean)
}