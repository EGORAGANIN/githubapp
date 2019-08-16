package ru.egoraganin.githubsample.model.data

class ServerException(val statusCode: Int, cause: Throwable? = null) : Exception(cause)