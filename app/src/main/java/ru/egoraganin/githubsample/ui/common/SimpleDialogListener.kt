package ru.egoraganin.githubsample.ui.common


abstract class SimpleDialogListener(
    private val chainedListener: SimpleDialogListener? = null
) {

    open fun onPositiveButtonClicked() {
        chainedListener?.onPositiveButtonClicked()
    }

    open fun onNegativeButtonClicked() {
        chainedListener?.onNegativeButtonClicked()
    }

    open fun onNeutralButtonClicked() {
        chainedListener?.onNeutralButtonClicked()
    }

    open fun onDismiss() {
        chainedListener?.onDismiss()
    }

    open fun onCancel() {
        chainedListener?.onCancel()
    }
}