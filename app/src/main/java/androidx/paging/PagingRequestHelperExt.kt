package androidx.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.egoraganin.githubsample.model.repository.repo.NetworkStatus

private fun getError(report: PagingRequestHelper.StatusReport): Throwable {
    return PagingRequestHelper.RequestType.values().mapNotNull { report.getErrorFor(it) }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkStatus> {
    val liveData = MutableLiveData<NetworkStatus>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkStatus.Running)
            report.hasError() -> liveData.postValue(NetworkStatus.Failed(getError(report)))
            else -> liveData.postValue(NetworkStatus.Success)
        }
    }
    return liveData
}
