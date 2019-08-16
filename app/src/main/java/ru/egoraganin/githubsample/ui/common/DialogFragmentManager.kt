package ru.egoraganin.githubsample.ui.common

import android.app.Dialog
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity

class DialogFragmentManager(
    private val activity: FragmentActivity
) {

    private var activeDialog: DialogInterface? = null

    fun showMessageDialog(
        @StringRes messageId: Int,
        @StringRes positiveButtonId: Int,
        listener: SimpleDialogListener? = null
    ) {
        val dialog = createDialog(messageId = messageId, positiveButtonId = positiveButtonId, listener = listener)

        activeDialog?.dismiss()
        dialog.show()
        activeDialog = dialog
    }

    fun dismissActiveDialog() {
        activeDialog?.dismiss()
        activeDialog = null
    }

    private fun createDialog(
        @StringRes titleId: Int? = null,
        @StringRes messageId: Int? = null,
        @StringRes positiveButtonId: Int? = null,
        @StringRes negativeButtonId: Int? = null,
        cancellable: Boolean = false,
        listener: SimpleDialogListener? = null
    ): Dialog {

        val dialog = AlertDialog.Builder(activity).apply {
            titleId?.let { setTitle(it) }
            messageId?.let { setMessage(it) }
            positiveButtonId?.let { setPositiveButton(it) { dialog, _ ->
                dialog.dismiss()
                listener?.onPositiveButtonClicked()
            } }
            negativeButtonId?.let { setNegativeButton(it) { dialog, _ ->
                dialog.dismiss()
                listener?.onPositiveButtonClicked()
            } }
            setCancelable(cancellable)
            setOnCancelListener { listener?.onCancel() }
        }.create()

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }
}
