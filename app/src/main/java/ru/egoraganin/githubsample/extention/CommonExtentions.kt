package ru.egoraganin.githubsample.extention

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import org.threeten.bp.Instant
import retrofit2.HttpException
import retrofit2.Response
import ru.egoraganin.githubsample.R
import ru.egoraganin.githubsample.model.data.ServerException
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import java.io.IOException
import java.util.concurrent.locks.Lock

fun Navigator.newRootScreen(screen: Screen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun getPrimaryColor(context: Context):  Int? {
    return getColorAttr(context, R.attr.colorPrimary)
}

fun getColorAttr(context: Context, attr: Int): Int? {
    val theme = context.theme
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))

    val emptyColor = -1
    val color = typedArray.getColor(0, emptyColor)
    typedArray.recycle()

    return if (color != emptyColor) color else null
}

fun View.isVisibleGone(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.isVisibleInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

inline fun <T> withLock(vararg locks: Lock, action: () -> T): T {
    for (lock in locks) {
        lock.lock()
    }
    try {
        return action()
    } finally {
        for (lock in locks.reversed()) {
            lock.unlock()
        }
    }
}

fun Instant.getTimeAgo(): CharSequence {
    return DateUtils.getRelativeTimeSpanString(toEpochMilli(), Instant.now().toEpochMilli(), DateUtils.SECOND_IN_MILLIS)
}

fun Throwable.getMessageStringRes() = when (this) {
    is HttpException -> when (code()) {
        401 -> R.string.message_http_401_unauthorized
        403 -> R.string.message_http_403_forbidden
        500 -> R.string.message_http_500_internal_server_error
        else -> R.string.message_unexpected_error
    }
    is IOException -> R.string.message_connection_exception
    else -> R.string.message_unexpected_error
}

fun <T> Response<T>.getServerException(cause: Throwable? = null): Exception? {
    val statusCode = code()
    return if (statusCode in 400..500) ServerException(statusCode, cause) else null
}